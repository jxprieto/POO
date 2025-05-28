package com.opensky.service;

public interface ClientService {
    void createClient(String name, Integer age, String email, String phone);
    void showItinerary(String id);
}
