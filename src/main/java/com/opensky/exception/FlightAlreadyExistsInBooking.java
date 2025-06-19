package com.opensky.exception;

public class FlightAlreadyExistsInBooking extends OpenSkyException {

    public FlightAlreadyExistsInBooking(String flightId, String bookingId) {
        super("Flight with ID " + flightId + " already exists in booking with ID " + bookingId + ".");
    }
}
