package com.opensky.service;

import com.opensky.model.Client;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.SQLClientRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ClientServiceImpl implements ClientService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    private final ClientRepository repo;
    private final Printer printer;

    public ClientServiceImpl(ClientRepository repo, Printer printer) {
        this.repo = repo;
        this.printer = printer;
    }

    public static ClientServiceImpl createInstance() {
        return new ClientServiceImpl(
                di.getDependency(SQLClientRepository.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    @Override
    public void createClient(String name, Integer age, String email, String phone) {
        if (name == null || name.isEmpty()) printer.print("Name cannot be null or empty");
        if (email == null || email.isEmpty()) printer.print("Email cannot be null or empty");
        if (phone == null || phone.isEmpty()) printer.print("Phone cannot be null or empty");
        repo.findByEmail(email)
                .ifPresent(_ -> {
                    printer.print("Client with email " + email + " already exists.");
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
