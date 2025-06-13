package com.opensky;

import com.opensky.execution.CommandExecutor;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.utils.DependencyInjector;
import com.opensky.utils.SingletonDependencyInjector;
import com.opensky.view.*;

import java.util.Map;
import java.util.Scanner;

import static com.opensky.view.Command.*;


public class OpenSkyCli {

    private static final DependencyInjector di = SingletonDependencyInjector.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Printer printer = di.getDependency(ConsolePrinter.class);
    private static final CommandExecutor executor = di.getDependency(CommandExecutor.class);

    private static final Map<String, Command> commands = Map.of(
            CREATE_CLIENT_COMMAND, di.getDependency(CreateClientViewCommand.class),
            CREATE_FLIGHT_COMMAND, di.getDependency(CreateFlightViewCommand.class),
            CREATE_BOOKING_COMMAND, di.getDependency(CreateBookingViewCommand.class),
            VIEW_ITINERARY_COMMAND, di.getDependency(ViewItineraryViewCommand.class),
            CANCEL_BOOKING_COMMAND, di.getDependency(CancelBookingViewCommand.class),
            MODIFY_BOOKING_COMMAND, di.getDependency(ModifyBookingViewCommand.class),
            SHOW_ALL_CLIENTS_COMMAND, di.getDependency(ShowAllClientsViewCommand.class)
    );
    private static Command defaultCommand = di.getDependency(DefaultViewCommand.class);


    public static void run(){
        String input;
        while (!(input = getOption()).equals(EXIT_OPTION)){
            executor.executeCommand(getCommand(input), input);
            waitAndCatchException();
        }
        printer.print("Exiting the application...\n");
    }

    private static void waitAndCatchException() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static Command getCommand(String input) {
        return commands.getOrDefault(
                input.split(ARGUMENT_SPLIT_REGEX)[0],
                defaultCommand
        );
    }

    private static String getOption(){
        printer.print(MENU);
        return scanner.nextLine();
    }
}
