package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ModifyBookingViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static ModifyBookingViewCommand createInstance() {
        return new ModifyBookingViewCommand(
                di.getDependency(DefaultBookingService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final BookingService service;
    private final Printer printer;

    private ModifyBookingViewCommand(BookingService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 4)
            throw new FormatDataException("Invalid input for " + MODIFY_BOOKING_COMMAND + " expected is: " + MODIFY_BOOKING);

        final String bookingId = args[1].split(COLON)[1];
        final String flightId = args[2].split(COLON)[1];
        final String numberOfSeats = args[3].split(COLON)[1];

        var booking = service.modifyBooking(bookingId, flightId, Integer.parseInt(numberOfSeats));
        printer.print("Booking updated successfully with ID: " + booking.getId() + "\nBooking details:\n");
        printer.print(booking + "\n");
    }
}
