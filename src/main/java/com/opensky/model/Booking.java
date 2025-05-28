package com.opensky.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder(toBuilder = true)
public class Booking extends Entity<String> {
    Client client;
    @Builder.Default
    List<Flight> flights = List.of();
    LocalDateTime bookingDate;
    @Builder.Default
    List<Integer> numberOfSeatsPerFlight = List.of();

    @ToString.Include(name = "id", rank = 1)
    public String getId() {
        return id;
    }
}
