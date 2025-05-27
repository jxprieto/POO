package com.opensky.view;

import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.ClientService;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateClientCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static final String WHITE_SPACE = "\\s*;+\\s*|\\s+";
    public static final String COLON = ":";

    private final ClientService service;
    private final Printer printer;

    public CreateClientCommand(ClientService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand(
                di.getDependency(DefaultClientService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(WHITE_SPACE);
        if (args.length != 5){
            printer.print("Invalid command format. Expected: createClient " +
                    "name:<name> age:<age> email:<email> phone:<phone>");
            return;
        }

        String name = args[1].split(COLON)[1];
        Integer age = Integer.parseInt(args[2].split(COLON)[1]);
        String email = args[3].split(COLON)[1];
        String phone = args[4].split(COLON)[1];
        service.createClient(name, age, email, phone);
    }
}