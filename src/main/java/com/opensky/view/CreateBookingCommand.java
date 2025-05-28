package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.service.BookingService;
import com.opensky.service.DefaultBookingService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;


public class CreateBookingCommand implements Command, Dependency {

    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CreateBookingCommand createInstance() {
        return new CreateBookingCommand(
                di.getDependency(DefaultBookingService.class)
        );
    }

    private final BookingService service;

    public CreateBookingCommand(BookingService service) {
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
