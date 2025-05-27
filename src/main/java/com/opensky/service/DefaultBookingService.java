package com.opensky.service;

import com.opensky.model.Booking;
import com.opensky.model.Flight;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.FlightRepository;
import com.opensky.repository.SQLBookingRepository;
import com.opensky.repository.SQLFlightRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class DefaultBookingService implements BookingService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static DefaultBookingService createInstance() {
        return new DefaultBookingService();
    }

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;


    public DefaultBookingService() {
        this.bookingRepository = di.getDependency(SQLBookingRepository.class);
        this.flightRepository = di.getDependency(SQLFlightRepository.class);
    }

    @Override
    public void createBooking(String origin, String arrival, int numberOfSeats) {
        final List<Flight> flights = flightRepository
                .findAll()
                .stream()
                .filter(flight -> flight.getAvailableSeats() >= numberOfSeats)
                .toList();
        final List<Flight> bookingFlights = getBestConnection(flights, origin, arrival);
        final Booking booking = new Booking(
                null, // TODO: Client should be passed here, but we don't have a client in this context
                //passengerName,
                bookingFlights,
                LocalDateTime.now(),
                numberOfSeats
        );
        bookingRepository.create(booking);
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
}
