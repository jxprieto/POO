package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class CancelBookingViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CancelBookingViewCommand createInstance() {
        return new CancelBookingViewCommand(
                di.getDependency(DefaultBookingService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final BookingService service;
    private final Printer printer;

    private CancelBookingViewCommand(BookingService bookingService, Printer printer) {
        this.service = bookingService;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 4)
            throw new FormatDataException("Invalid input for " + CANCEL_BOOKING_COMMAND + " expected is: " + CANCEL_BOOKING);
        final String bookingId = args[1].split(COLON)[1];
        service.cancelBooking(bookingId);
        printer.print("Booking with ID " + bookingId + " has been successfully cancelled.\n");
    }
}
