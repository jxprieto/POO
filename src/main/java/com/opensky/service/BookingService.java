package com.opensky.service;

public interface BookingService {
    void createBooking(String origin, String arrival, int numberOfSeats);

    void cancelBooking(String bookingId);
}
