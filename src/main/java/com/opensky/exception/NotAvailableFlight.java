package com.opensky.exception;

public class NotAvailableFlight extends RuntimeException {
    public NotAvailableFlight(String origin, String arrival, int numberOfSeats) {
        super(String.format("No available flights from %s to %s with %d seats.", origin, arrival, numberOfSeats));
    }

    public NotAvailableFlight(String flightId, int numberOfSeats) {
        super(String.format("Flight with id %s does not have enough available seats: %d requested.", flightId, numberOfSeats));
    }
}
