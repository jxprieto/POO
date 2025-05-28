package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.ClientService;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateClientViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CreateClientViewCommand createInstance() {
        return new CreateClientViewCommand(
                di.getDependency(DefaultClientService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final ClientService service;
    private final Printer printer;

    private CreateClientViewCommand(ClientService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 5)
            throw new FormatDataException("Invalid input for " + CREATE_CLIENT_COMMAND + " expected is: " + CREATE_CLIENT);

        String name = getArgValue(args[1]);
        int age = Integer.parseInt(getArgValue(args[2]));
        String email = getArgValue(args[3]);
        String phone = getArgValue(args[4]);

        if (age < 0) throw new FormatDataException("Age cannot be negative");
        if (name == null || name.isEmpty()) throw new FormatDataException("Name cannot be null or empty");
        if (email == null || !email.matches(EMAIL_REGEX)) throw new FormatDataException("Email should be a valid email address");
        if (phone == null || phone.isEmpty()) throw new FormatDataException("Phone cannot be null or empty");

        var client = service.createClient(name, age, email, phone);
        printer.print("Client created successfully with ID: " + client.getId() + "\nClient details:\n");
        printer.print(client + "\n");
    }
}