package com.opensky.service;

import com.opensky.model.Flight;
import com.opensky.repository.FlightRepository;
import com.opensky.repository.sql.SQLFlightRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;

public class DefaultFlightService implements FlightService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static DefaultFlightService createInstance() {
        return new DefaultFlightService(
                di.getDependency(SQLFlightRepository.class)
        );
    }

    private final FlightRepository flightRepository;

    private DefaultFlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Flight createFlight(String flightNumber, String origin, String destination, LocalDateTime departure, LocalDateTime arrival, Integer availableSeats) {
        var flight = Flight
                .builder()
                .flightNumber(flightNumber)
                .origin(origin)
                .destination(destination)
                .departureTime(departure)
                .arrivalTime(arrival)
                .availableSeats(availableSeats)
                .build();
        return flightRepository.create(flight);
    }
}
