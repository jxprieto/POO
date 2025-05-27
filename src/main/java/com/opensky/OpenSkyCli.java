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
    
    public static final String CREATE_CLIENT_OPTION = "createClient";
    public static final String CREATE_FLIGHT_OPTION = "createFlight";
    public static final String CREATE_RESERVATION_OPTION = "createReservation";
    public static final String VIEW_ITINERARY_OPTION = "viewItinerary";
    public static final String CANCEL_RESERVATION_OPTION = "cancelReservation";
    public static final String MODIFY_RESERVATION_OPTION = "modifyReservation";
    public static final String EXIT_OPTION = "exit";

    private static final String MENU = String.format("""
        Please enter a command (date format is yyyy-MM-dd HH:mm)
            -> %s name:<nombre>; edad:<edad>;email:<correo>;phone:<teléfono>
            -> %s flightNumber:<númeroVuelo>;origin:<origen>;destination:<destino>;departure:<fecha y hora de salida>;arrival:<fecha y hora de llegada>;availableSeats:<número de asientos disponibles>
            -> %s origin:<origin>;arrival:<arrival>;seats:<número de asientos>
            -> %s clientId:<id cliente>
            -> %s reservationId:<id reserva>
            -> %s reservationId:<id reserva>;newFlightId:<id nuevo vuelo>;newSeats:<número de asientos>
            -> %s
    """, CREATE_CLIENT_OPTION, CREATE_FLIGHT_OPTION, CREATE_RESERVATION_OPTION, VIEW_ITINERARY_OPTION,
            CANCEL_RESERVATION_OPTION, MODIFY_RESERVATION_OPTION, EXIT_OPTION);
    
    private static final Map<String, Command> commands = Map.of(
            CREATE_CLIENT_OPTION, dependencyInjector.getDependency(CreateClientCommand.class),
            CREATE_FLIGHT_OPTION, dependencyInjector.getDependency(CreateFlightCommand.class),
            CREATE_RESERVATION_OPTION, dependencyInjector.getDependency(CreateBookingCommand.class),
            VIEW_ITINERARY_OPTION, dependencyInjector.getDependency(ViewItineraryCommand.class),
            CANCEL_RESERVATION_OPTION, dependencyInjector.getDependency(CancelBookingCommand.class),
            MODIFY_RESERVATION_OPTION, dependencyInjector.getDependency(ModifyBookingCommand.class)
    );


    public static void run(){
        String option;
        while (!(option = getOption()).equals(EXIT_OPTION))
            getCommand(option).execute(option);// cambiar para meter un error handler y pasarle la lambda
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
