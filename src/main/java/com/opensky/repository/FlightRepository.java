package com.opensky.repository;

import com.opensky.model.Flight;

import java.util.Map;

public interface FlightRepository extends GenericRepository<Flight> {
    Map<Flight, Integer> getAllFlightsByBookingId(String id);
}
