package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ShowAllClientsViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    public static ShowAllClientsViewCommand createInstance() {
        return new ShowAllClientsViewCommand(
                di.getDependency(DefaultClientService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final DefaultClientService service;
    private final ConsolePrinter printer;

    private ShowAllClientsViewCommand(DefaultClientService service, ConsolePrinter printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 1)
            throw new FormatDataException("Invalid input for " + SHOW_ALL_CLIENTS_COMMAND + ", it shouldn't have any argument" );

        var clients = service.getAllClients();
        if (clients.isEmpty())
            printer.print("No clients found.\n");
        else{
            printer.print("Client details:");
            clients.forEach(c -> printer.print(c.toString()));
            printer.print("\n");
        }
    }
}
