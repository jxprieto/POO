package com.opensky.service;

import com.opensky.model.Booking;
import com.opensky.model.Client;

import java.util.List;

public interface ClientService {
    Client createClient(String name, Integer age, String email, String phone);
    List<Booking> getAllClientBookings(String id);
    List<Client> getAllClients();
}
