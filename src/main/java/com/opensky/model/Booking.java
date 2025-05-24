package com.opensky.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder(toBuilder = true)
public class Booking extends Entity {
    Client client;
    Flight flight;
    Integer numberOfSeats;
}
