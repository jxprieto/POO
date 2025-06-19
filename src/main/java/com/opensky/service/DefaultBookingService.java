package com.opensky.service;

import com.opensky.exception.EntityNotFoundException;
import com.opensky.exception.NotAvailableFlightException;
import com.opensky.model.Booking;
import com.opensky.model.Flight;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.FlightRepository;
import com.opensky.repository.sql.SQLBookingRepository;
import com.opensky.repository.sql.SQLClientRepository;
import com.opensky.repository.sql.SQLFlightRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DefaultBookingService implements BookingService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static DefaultBookingService createInstance() {
        return new DefaultBookingService(
                di.getDependency(SQLBookingRepository.class),
                di.getDependency(SQLFlightRepository.class),
                di.getDependency(SQLClientRepository.class)
        );
    }

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final ClientRepository clientRepository;

    private DefaultBookingService(final BookingRepository bookingRepository, final FlightRepository flightRepository,
                                  final ClientRepository clientRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Booking createBooking(String origin, String arrival, int numberOfSeats, String clientId) {
        final List<Flight> flights = flightRepository
                .findAll()
                .stream()
                .filter(flight -> flight.getAvailableSeats() >= numberOfSeats)
                .toList();
        final List<Flight> bookingFlights = getBestConnection(flights, origin, arrival);
        if (bookingFlights.isEmpty())
            throw new NotAvailableFlightException(origin, arrival, numberOfSeats);
        var booking = Booking
                .builder()
                .client(clientRepository.read(clientId)
                        .orElseThrow(() -> new EntityNotFoundException("Client not found for id: " + clientId)))
                .flights(bookingFlights)
                .bookingDate(LocalDateTime.now())
                .numberOfSeatsPerFlight(Collections.nCopies(bookingFlights.size(), numberOfSeats))
                .build();
        return bookingRepository.create(booking);
    }

    private List<Flight> getBestConnection(List<Flight> flights, String origin, String arrival) {

        final List<Flight> flightsFromOrigin = flights.stream()
                .filter(flight -> flight.getOrigin().equals(origin))
                .toList();
        final List<Flight> differentOriginFlights = excludeFlightsFromOrigin(origin, flights);
        return Collections.unmodifiableList(
                findBestPathFromCandidates(flightsFromOrigin, differentOriginFlights, arrival)
        );

    }


    private List<Flight> findBestPathFromCandidates(List<Flight> candidates, List<Flight> flightsToExplore,
                                                    String arrival) {
        List<Flight> bestPath = List.of();
        for (Flight current : candidates) {
            if (current.getDestination().equals(arrival)) return List.of(current);
            final List<Flight> validFlightsFromOrigin = flightsToExplore.stream()
                    .filter(f -> f.getOrigin().equals(current.getDestination()) && validFlight(current, f))
                    .toList();
            final List<Flight> differentOriginFlights = excludeFlightsFromOrigin(current.getOrigin(), flightsToExplore);
            final List<Flight> nextPath = findBestPathFromCandidates(validFlightsFromOrigin, differentOriginFlights, arrival);
            if (pathIsBetterThanPrevious(nextPath, bestPath))
                bestPath = Stream.concat(Stream.of(current), nextPath.stream()).toList();
        }
        return bestPath;
    }

    private static boolean pathIsBetterThanPrevious(List<Flight> nextPath, List<Flight> previousFullPath) {
        return !nextPath.isEmpty() && (previousFullPath.isEmpty() || nextPath.size() + 1 < previousFullPath.size());
    }

    private static List<Flight> excludeFlightsFromOrigin(String originToExclude, List<Flight> flightsFromOrigin) {
        return flightsFromOrigin.stream()
                .filter(flight -> !flight.getOrigin().equals(originToExclude))
                .toList();
    }

    private boolean validFlight(Flight flight, Flight nextFlight) {
        return nextFlight.getDepartureTime().isAfter(flight.getArrivalTime().plusHours(1).minusSeconds(1));
    }

    @Override
    public void cancelBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public Booking modifyBooking(String bookingId, String flightId, int numberOfSeats) {
        var booking = bookingRepository.read(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found for id: " + bookingId));
        var flight = flightRepository.read(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found for id: " + flightId));
        if (booking.getFlights().contains(flight))
            throw new EntityNotFoundException("Flight already exists in booking: " + bookingId);
        var newFlights = new ArrayList<>(booking.getFlights());
        var newNumberOfSeats = new ArrayList<>(booking.getNumberOfSeatsPerFlight());

        newNumberOfSeats.add(numberOfSeats);
        newFlights.add(flight);

        var toUpdate = booking
                .toBuilder()
                .flights(newFlights)
                .numberOfSeatsPerFlight(newNumberOfSeats)
                .build();
        return bookingRepository.update(toUpdate);

    }
}
