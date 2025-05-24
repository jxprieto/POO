package com.opensky.command;

import com.opensky.service.ClientService;
import com.opensky.service.ClientServiceImpl;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateClientCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static final String WHITE_SPACE = " ";
    public static final String COLON = ":";

    private final ClientService service;

    public CreateClientCommand(ClientService service) {
        this.service = service;
    }

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand(di.getDependency(ClientServiceImpl.class));
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(WHITE_SPACE);
        if (args.length != 4)
            throw new IllegalArgumentException(
                    "Invalid command format. Expected: createClient " +
                            "name:<name> age:<age> email:<email> phone:<phone>");
        String name = args[1].split(COLON)[1];
        Integer age = Integer.parseInt(args[2].split(COLON)[1]);
        String email = args[3].split(COLON)[1];
        String phone = args[4].split(COLON)[1];
        service.createClient(name, age, email, phone);
    }
}