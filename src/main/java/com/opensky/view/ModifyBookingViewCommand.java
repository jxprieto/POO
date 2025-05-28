package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ModifyBookingViewCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static ModifyBookingViewCommand createInstance() {
        return new ModifyBookingViewCommand(
                di.getDependency(DefaultBookingService.class)
        );
    }

    private final BookingService service;

    private ModifyBookingViewCommand(BookingService service) {
        this.service = service;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 4)
            throw new FormatDataException("Invalid input for " + MODIFY_BOOKING_COMMAND + " expected is: " + MODIFY_BOOKING);

        final String bookingId = args[1].split(COLON)[1];
        final String flightId = args[2].split(COLON)[1];
        final String numberOfSeats = args[3].split(COLON)[1];

        service.modifyBooking(bookingId, flightId, Integer.parseInt(numberOfSeats));
    }
}
