package com.opensky.service;

import com.opensky.model.Flight;
import com.opensky.repository.FlightRepository;
import com.opensky.repository.sql.SQLFlightConnectionManager;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;

public class DefaultFlightService implements FlightService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public DefaultFlightService() {
        this.flightRepository = di.getDependency(SQLFlightConnectionManager.class);
    }

    public static DefaultFlightService createInstance() {
        return new DefaultFlightService();
    }

    private final FlightRepository flightRepository;

    @Override
    public void createFlight(String flightNumber, String origin, String destination, LocalDateTime departure, LocalDateTime arrival, Integer availableSeats) {
        Flight flight = new Flight(flightNumber, origin, destination, departure, arrival, availableSeats);
        flightRepository.create(flight);
    }
}
