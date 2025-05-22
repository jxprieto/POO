package com.opensky;

import com.opensky.command.*;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.utils.DependencyInjector;
import com.opensky.utils.SingletonDependencyInjector;

import java.util.Map;
import java.util.Scanner;

public class OpenSkyCli {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Printer printer = new ConsolePrinter();
    private static final DependencyInjector dependencyInjector = SingletonDependencyInjector.getInstance();

    private static final String MENU = """
        Please enter a command:
            -> createClient name:<nombre>; edad:<edad>;email:<correo>;
                phone:<teléfono>
            -> createFlight flightNumber:<númeroVuelo>;origin:<origen>;
                destination:<destino>;departure:<fecha y hora de salida>;
                arrival:<fecha y hora de llegada>;
                availableSeats:<número de asientos disponibles>
            ->  createReservation origin:<origin>;arrival:<arrival>;
                seats:<número de asientos>
            ->  viewItinerary clientId:<id cliente>
            ->  cancelReservation reservationId:<id reserva>
            ->  modifyReservation reservationId:<id reserva>;
                newFlightId:<id nuevo vuelo>;newSeats:<número de asientos>
            -> exit
    """;

    private static final Map<String, Command> commands = Map.of(
            "createClient", dependencyInjector.getDependency(CreateClientCommand.class),
            "createFlight", dependencyInjector.getDependency(CreateFlightCommand.class),
            "createReservation", dependencyInjector.getDependency(CreateBookingCommand.class),
            "viewItinerary", dependencyInjector.getDependency(ViewItineraryCommand.class),
            "cancelReservation", dependencyInjector.getDependency(CancelBookingCommand.class),
            "modifyReservation", dependencyInjector.getDependency(ModifyBookingCommand.class)
    );

    public static void startLoop(){
        String option;
        while (!(option = getOption()).equals("exit"))
            getCommand(option).execute(option);
        printer.print("Exiting the application...\n");
    }


    private static Command getCommand(String option) {
        return commands.get(
                option.split(" ")[0]
        );
    }

    private static String getOption(){
        printer.print(MENU);
        return scanner.nextLine();
    }
}
