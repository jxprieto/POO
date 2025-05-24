package com.opensky.service;

import com.opensky.model.Client;
import com.opensky.repository.GenericRepository;

import java.util.Optional;

public interface ClientRepository extends GenericRepository<com.opensky.model.Client> {
    Optional<Client> findByEmail(String email);
}
