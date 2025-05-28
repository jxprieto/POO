package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.DefaultFlightService;
import com.opensky.service.FlightService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateFlightViewCommand implements Command, Dependency {

    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static CreateFlightViewCommand createInstance() {
        return new CreateFlightViewCommand(
                di.getDependency(DefaultFlightService.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final FlightService service;
    private final Printer printer;

    private CreateFlightViewCommand(FlightService service, Printer printer) {
        this.service = service;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 7)
            throw new FormatDataException("Invalid input for " + CREATE_FLIGHT_COMMAND + " expected is: " + CREATE_FLIGHT);

        final String flightNumber = args[1].split(COLON)[1];
        final String origin = args[2].split(COLON)[1];
        final String destination = args[3].split(COLON)[1];
        final LocalDateTime departure = LocalDateTime.parse(args[4].split(COLON)[1], formater);
        final LocalDateTime arrival = LocalDateTime.parse(args[5].split(COLON)[1], formater);
        final int availableSeats = Integer.parseInt(args[6].split(COLON)[1]);

        if (availableSeats < 0) throw new FormatDataException("Available seats cannot be negative");
        if (!departure.isAfter(LocalDateTime.now()) || !arrival.isAfter(LocalDateTime.now()))
            throw new FormatDataException("Departure and arrival times must be in the future");

        var flight = service.createFlight(flightNumber, origin, destination, departure, arrival, availableSeats);
        printer.print("Flight created successfully with ID: " + flight.getId() + "\nFlight details:\n");
        printer.print(flight + "\n");
    }
}
