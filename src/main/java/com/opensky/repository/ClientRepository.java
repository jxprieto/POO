package com.opensky.repository;

import com.opensky.model.Client;

import java.util.Optional;

public interface ClientRepository extends GenericRepository<Client> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByNumber(String phone);
}
