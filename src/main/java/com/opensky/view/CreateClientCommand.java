package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.service.ClientService;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateClientCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand(
                di.getDependency(DefaultClientService.class)
        );
    }

    private final ClientService service;

    public CreateClientCommand(ClientService service) {
        this.service = service;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 5)
            throw new FormatDataException("Invalid input for " + CREATE_CLIENT_COMMAND + " expected is: " + CREATE_CLIENT);

        String name = args[1].split(COLON)[1];
        int age = Integer.parseInt(args[2].split(COLON)[1]);
        String email = args[3].split(COLON)[1];
        String phone = args[4].split(COLON)[1];

        if (age < 0) throw new FormatDataException("Age cannot be negative");
        if (name == null || name.isEmpty()) throw new FormatDataException("Name cannot be null or empty");
        if (email == null || email.matches(EMAIL_REGEX)) throw new FormatDataException("Email cannot be null or empty");
        if (phone == null || phone.isEmpty()) throw new FormatDataException("Phone cannot be null or empty");

        service.createClient(name, age, email, phone);
    }
}