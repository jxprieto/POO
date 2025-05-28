package com.opensky.service;

import com.opensky.model.Booking;

import java.util.List;

public interface ClientService {
    void createClient(String name, Integer age, String email, String phone);
    List<Booking> getAllClientBookings(String id);
}
