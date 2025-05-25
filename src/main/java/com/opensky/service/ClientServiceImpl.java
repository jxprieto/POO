package com.opensky.service;

import com.opensky.model.Client;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.SQLClientRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ClientServiceImpl implements ClientService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    private final ClientRepository repo;

    public ClientServiceImpl(ClientRepository repo) {
        this.repo = repo;
    }

    public static ClientServiceImpl createInstance() {
        return new ClientServiceImpl(di.getDependency(SQLClientRepository.class));
    }

    @Override
    public void createClient(String name, Integer age, String email, String phone) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        if (phone == null || phone.isEmpty()) throw new IllegalArgumentException("Phone cannot be null or empty");
        repo.findByEmail(email)
                .ifPresent(_ -> {
                    throw new IllegalArgumentException("Email already exists: " + email);
                });
        repo.create(Client
                .builder()
                .name(name)
                .age(age)
                .email(email)
                .phoneNumber(phone)
                .build());
    }

}
