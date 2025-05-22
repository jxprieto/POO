package com.opensky;

import com.opensky.command.*;

import java.util.Map;
import java.util.Scanner;

public class OpenSkyCli {
    private static Scanner scanner = new Scanner(System.in);
    private static final Map<String, Command> commands = Map.of(
            "createClient", CreateClientCommand.getInstance(),
            "createFlight", CreateFlightCommand.getInstance(),
            "createReservation", CreateBookingCommand.getInstance(),
            "viewItinerary", ViewItineraryCommand.getInstance(),
            "cancelReservation", CancelBookingCommand.getInstance(),
            "modifyReservation", ModifyBookingCommand.getInstance()
    );

    public static void startLoop(){
        Command command;
        while (((command = getCommand()) != null)) command.execute();
        ExitCommand.getInstance().execute();
    }


    private static Command getCommand() {
        return commands.get(getOption());
    }

    public static String getOption(){
        System.out.println("Please enter a command: ");
        return scanner.nextLine();
    }
}
