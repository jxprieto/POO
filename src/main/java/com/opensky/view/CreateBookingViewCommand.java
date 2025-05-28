package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;


public class CreateBookingViewCommand implements Command, Dependency {

    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CreateBookingViewCommand createInstance() {
        return new CreateBookingViewCommand(
                di.getDependency(DefaultBookingService.class)
        );
    }

    private final BookingService service;

    private CreateBookingViewCommand(BookingService service) {
        this.service = service;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 4)
            throw new FormatDataException("Invalid input for " + CREATE_BOOKING_COMMAND + " expected is: " + CREATE_BOOKING);
        final String origin = args[1].split(COLON)[1];
        final String arrival = args[2].split(COLON)[1];
        final int numberOfSeats = Integer.parseInt(args[3].split(COLON)[1]);

        service.createBooking(origin, arrival, numberOfSeats);
    }

}
