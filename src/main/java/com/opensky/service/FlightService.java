package com.opensky.service;

import java.time.LocalDateTime;

public interface FlightService {
    void createFlight(String flightNumber, String origin, String destination, LocalDateTime departure, LocalDateTime arrival, Integer availableSeats);
}
