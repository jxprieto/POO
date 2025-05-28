package com.opensky.service;

import com.opensky.exception.EntityNotFoundException;
import com.opensky.exception.NotAvailableFlightException;
import com.opensky.model.Booking;
import com.opensky.model.Flight;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.FlightRepository;
import com.opensky.repository.sql.SQLBookingRepository;
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
                di.getDependency(SQLFlightRepository.class)
        );
    }

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    private DefaultBookingService(final BookingRepository bookingRepository, final FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public Booking createBooking(String origin, String arrival, int numberOfSeats) {
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
                .client(null)
                .flights(bookingFlights)
                .bookingDate(LocalDateTime.now())
                .numberOfSeatsPerFlight(Collections.nCopies(bookingFlights.size(), numberOfSeats))
                .build();
        bookingRepository.create(booking);
        return booking;
    }

    private List<Flight> getBestConnection(List<Flight> flights, String origin, String arrival) {

        final List<Flight> flightsFromOrigin = flights.stream()
                .filter(flight -> flight.getOrigin().equals(origin))
                .toList();
        final List<Flight> differentOriginFlights = excludeFlightsFromOrigin(origin, flightsFromOrigin);
        return findBestPathFromCandidates(flightsFromOrigin, differentOriginFlights, arrival);

    }

    private List<Flight> explorePathFromFlight(Flight previousFlight, List<Flight> flightsToExplore, String arrival) {
        if (previousFlight.getDestination().equals(arrival)) return List.of(previousFlight);
        final List<Flight> validFlightsFromOrigin = flightsToExplore.stream()
                .filter(f -> f.getOrigin().equals(previousFlight.getOrigin()) && validFlight(previousFlight, f))
                .toList();
        final List<Flight> differentOriginFlights = excludeFlightsFromOrigin(previousFlight.getOrigin(), flightsToExplore);
        return findBestPathFromCandidates(validFlightsFromOrigin, differentOriginFlights, arrival);
    }

    private List<Flight> findBestPathFromCandidates(List<Flight> candidates, List<Flight> flightsToExplore,
                                                    String arrival) {
        List<Flight> bestPath = List.of();
        for (Flight current : candidates) {
            List<Flight> nextPath = explorePathFromFlight(current, flightsToExplore, arrival);
            if (pathIsBetterThanPrevious(nextPath, bestPath))
                bestPath = Stream.concat(Stream.of(current), nextPath.stream()).toList();
        }
        return bestPath;
    }


    private static boolean pathIsBetterThanPrevious(List<Flight> nextPath, List<Flight> bestFullPath) {
        return !nextPath.isEmpty() && (bestFullPath.isEmpty() || nextPath.size() + 1 < bestFullPath.size());
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
