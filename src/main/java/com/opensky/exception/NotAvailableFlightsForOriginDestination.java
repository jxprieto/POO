package com.opensky.exception;

public class NotAvailableFlightsForOriginDestination extends RuntimeException {
    public NotAvailableFlightsForOriginDestination(String origin, String arrival, int numberOfSeats) {
        super(String.format("No available flights from %s to %s with %d seats.", origin, arrival, numberOfSeats));
    }
}
