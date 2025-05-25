package com.opensky.repository;

import com.opensky.model.Entity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends Entity> {
    T create(T entity);
    T update(T entity);
    Optional<T> read(String id);
    void deleteById(String id);
    List<T> findAll();
}
