package com.opensky.repository;

import com.opensky.model.Flight;

import java.util.List;

public interface FlightRepository extends GenericRepository<Flight> {
    List<Flight> getAllFlightsByBookingId(String id);
}
