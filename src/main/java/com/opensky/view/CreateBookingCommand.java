package com.opensky.view;

import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CreateBookingCommand implements Command, Dependency {

    public static CreateBookingCommand createInstance() {
        return new CreateBookingCommand();
    }

    public static final String WHITE_SPACE = "\\s*;+\\s*|\\s+";
    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    public static final String COLON = ":";

    private final BookingService service;
    private final Printer printer;

    public CreateBookingCommand() {
        this.service = di.getDependency(DefaultBookingService.class);
        this.printer = di.getDependency(ConsolePrinter.class);
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(WHITE_SPACE);
        if (args.length != 4){
            printer.print("origin:<origin>;arrival:<arrival>;seats:<número de asientos>");
            return;
        }
        final String origin = args[1].split(COLON)[1];
        final String arrival = args[2].split(COLON)[1];
        final int numberOfSeats = Integer.parseInt(args[3].split(COLON)[1]);

        service.createBooking(origin, arrival, numberOfSeats);
    }

}
