package com.opensky.view;

import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.service.DefaultFlightService;
import com.opensky.service.FlightService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateFlightCommand implements Command, Dependency {

    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final FlightService service;

    public static CreateFlightCommand createInstance() {
        return new CreateFlightCommand();
    }

    public static final String WHITE_SPACE = "\\s*;+\\s*|\\s+";
    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    public static final String COLON = ":";

    private final Printer printer;

    public CreateFlightCommand() {
        this.service = di.getDependency(DefaultFlightService.class);
        this.printer = di.getDependency(ConsolePrinter.class);
    }

    @Override
    public void execute(String command) {
        String[] args = command.split(WHITE_SPACE);
        if (args.length != 7){
            printer.print("flightNumber:<númeroVuelo>;origin:<origen>;destination:<destino>" +
                    ";departure:<fecha y hora de salida>;arrival:<fecha y hora de llegada>" +
                    ";availableSeats:<número de asientos disponibles>");
            return;
        }

        final String flightNumber = args[1].split(COLON)[1];
        final String origin = args[2].split(COLON)[1];
        final String destination = args[3].split(COLON)[1];
        final LocalDateTime departure = LocalDateTime.parse(args[4].split(COLON)[1], formater);
        final LocalDateTime arrival = LocalDateTime.parse(args[5].split(COLON)[1], formater);
        final Integer availableSeats = Integer.parseInt(args[6].split(COLON)[1]);

        service.createFlight(flightNumber, origin, destination, departure, arrival, availableSeats);

    }
}
