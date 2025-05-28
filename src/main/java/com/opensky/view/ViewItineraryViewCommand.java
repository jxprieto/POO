package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.ClientService;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ViewItineraryViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static ViewItineraryViewCommand createInstance() {
        return new ViewItineraryViewCommand(
                di.getDependency(DefaultClientService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final ClientService service;
    private final Printer printer;

    private ViewItineraryViewCommand(ClientService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        final String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 2)
            throw new FormatDataException("Invalid input, expected for " + VIEW_ITINERARY_COMMAND + "is: " + VIEW_ITINERARY);
        final String id = args[1].split(COLON)[1];
        if (id == null || id.isEmpty()) throw new FormatDataException("ID cannot be null or empty");
        var bookings = service.getAllClientBookings(id);
        printer.print("Client with id " + id + " has the following bookings:");
        bookings.forEach(booking -> printer.print(booking.toString()));
    }
}
