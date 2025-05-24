package com.opensky.repository;

import com.opensky.model.Client;
import com.opensky.service.ClientRepository;
import com.opensky.utils.Dependency;

import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository, Dependency {

    public static ClientRepositoryImpl createInstance() {
        return new ClientRepositoryImpl();
    }

    @Override
    public Client create(Client entity) {
        return null;
    }

    @Override
    public Client update(Client entity) {
        return null;
    }

    @Override
    public Optional<Client> read(Integer id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return Optional.empty();
    }
}
