package com.opensky.service;

import com.opensky.model.Flight;

import java.time.LocalDateTime;

public interface FlightService {
    Flight createFlight(String flightNumber, String origin, String destination, LocalDateTime departure, LocalDateTime arrival, Integer availableSeats);
}
