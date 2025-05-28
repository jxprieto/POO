package com.opensky.service;

import com.opensky.model.Booking;

public interface BookingService {
    Booking createBooking(String origin, String arrival, int numberOfSeats);

    void cancelBooking(String bookingId);

    Booking modifyBooking(String bookingId, String flightId, int numberOfSeats);
}
