package com.opensky;

import com.opensky.view.*;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.utils.DependencyInjector;
import com.opensky.utils.SingletonDependencyInjector;

import java.util.Map;
import java.util.Scanner;

import static com.opensky.view.Command.*;


public class OpenSkyCli {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Printer printer = new ConsolePrinter();
    private static final DependencyInjector dependencyInjector = SingletonDependencyInjector.getInstance();

    private static final Map<String, Command> commands = Map.of(
            CREATE_CLIENT_COMMAND, dependencyInjector.getDependency(CreateClientCommand.class),
            CREATE_FLIGHT_COMMAND, dependencyInjector.getDependency(CreateFlightCommand.class),
            CREATE_BOOKING_COMMAND, dependencyInjector.getDependency(CreateBookingCommand.class),
            VIEW_ITINERARY_COMMAND, dependencyInjector.getDependency(ViewItineraryCommand.class),
            CANCEL_BOOKING_COMMAND, dependencyInjector.getDependency(CancelBookingCommand.class),
            MODIFY_BOOKING_COMMAND, dependencyInjector.getDependency(ModifyBookingCommand.class)
    );


    public static void run(){
        String input;
        while (!(input = getOption()).equals(EXIT_OPTION))
            getCommand(input).execute(input); // cambiar para meter un error handler y pasarle la lambda
        printer.print("Exiting the application...\n");
    }


    private static Command getCommand(String input) {
        return commands.get(
                input.split(ARGUMENT_SPLIT_REGEX)[0]
        );
    }

    private static String getOption(){
        printer.print(MENU);
        return scanner.nextLine();
    }
}
