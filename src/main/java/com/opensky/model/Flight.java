package com.opensky.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder(toBuilder = true)
public class Flight extends Entity {
    String flightNumber;
    String origin;
    String destination;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    Integer availableSeats;
}
