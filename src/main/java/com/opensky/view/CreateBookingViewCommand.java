package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;


public class CreateBookingViewCommand implements Command, Dependency {

    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CreateBookingViewCommand createInstance() {
        return new CreateBookingViewCommand(
                di.getDependency(DefaultBookingService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final BookingService service;
    private final Printer printer;

    private CreateBookingViewCommand(BookingService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 5)
            throw new FormatDataException("Invalid input for " + CREATE_BOOKING_COMMAND + " expected is: " + CREATE_BOOKING);
        final String origin = getArgValue(args[1]);
        final String arrival = getArgValue(args[2]);
        final int numberOfSeats = Integer.parseInt(getArgValue(args[3]));
        final String clientId = getArgValue(args[4]);

        var booking = service.createBooking(origin, arrival, numberOfSeats, clientId);
        printer.print("Booking created successfully with ID: " + booking.getId() + "\nBooking details:\n");
        printer.print(booking + "\n");
    }

}
