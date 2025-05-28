package com.opensky.repository;

import com.opensky.model.Booking;

import java.util.List;

public interface BookingRepository extends GenericRepository<Booking> {
    List<Booking> findBokingsByClientId(String id);
}
