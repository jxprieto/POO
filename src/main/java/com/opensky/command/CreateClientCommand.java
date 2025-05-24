package com.opensky.command;

import com.opensky.model.Client;
import com.opensky.service.ClientService;
import com.opensky.service.ClientServiceImpl;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateClientCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    private final ClientService service;

    public static final String COLON = ":";

    public CreateClientCommand(ClientService service) {
        this.service = service;
    }

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand(di.getDependency(ClientServiceImpl.class));
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(" ");
        if (args.length != 4)
            throw new IllegalArgumentException(
                    "Invalid command format. Expected: createClient " +
                            "name:<name> age:<age> email:<email> phone:<phone>");
        Integer age = Integer.parseInt(args[1].split(COLON)[1]);
        Client client = Client
                .builder()
                .age(age)
                .email(args[2].split(COLON)[1])
                .name(args[3].split(COLON)[1])
                .build();
        service.createClient(client);
    }
}