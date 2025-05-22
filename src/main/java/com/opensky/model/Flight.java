package com.opensky.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Flight {
    String id;
    String flightNumber;
    String origin;
    String destination;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    Integer availableSeats;
}
