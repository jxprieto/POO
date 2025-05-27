package com.opensky.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder(toBuilder = true)
public class Booking extends Entity<String> {
    Client client;
    List<Flight> flights;
    LocalDateTime bookingDate;
    Integer numberOfSeats;
}
