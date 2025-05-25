package com.opensky.repository;

import com.opensky.model.Booking;
import com.opensky.utils.Dependency;

import java.util.List;
import java.util.Optional;

public class BookingRepositoryImpl implements BookingRepository, Dependency {

    public static BookingRepositoryImpl createInstance() {
        return new BookingRepositoryImpl();
    }

    private BookingRepositoryImpl() {}

    @Override
    public Booking create(Booking entity) {
        return null;
    }

    @Override
    public Booking update(Booking entity) {
        return null;
    }

    @Override
    public Optional<Booking> read(String id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<Booking> findAll() {
        return List.of();
    }
}
