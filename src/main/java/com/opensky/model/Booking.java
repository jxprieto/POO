package com.opensky.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Booking {
    String id;
    Client client;
    Flight flight;
    Integer numberOfSeats;
}
