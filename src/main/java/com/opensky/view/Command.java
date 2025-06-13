package com.opensky.view;

public interface Command {

    String ARGUMENT_SPLIT_REGEX = "\\s*;+\\s*|\\s+";
    String COLON = ":";
    String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    String VIEW_ITINERARY = "clientId:<id cliente>";
    String VIEW_ITINERARY_COMMAND = "viewItinerary";

    String CREATE_FLIGHT = "flightNumber:<númeroVuelo>;origin:<origen>;destination:<destino>;" +
            "departure:<fecha y hora de salida>;arrival:<fecha y hora de llegada>;" +
            "availableSeats:<número de asientos disponibles>";
    String CREATE_FLIGHT_COMMAND = "createFlight";

    String CREATE_CLIENT = "name:<name>;age:<age>;email:<email>;phone:<phone>";
    String CREATE_CLIENT_COMMAND = "createClient";

    String CREATE_BOOKING = "origin:<origin>;arrival:<arrival>;seats:<número de asientos>";
    String CREATE_BOOKING_COMMAND = "createBooking";

    String CANCEL_BOOKING = "reservationId:<id reserva>";
    String CANCEL_BOOKING_COMMAND = "cancelReservation";

    String MODIFY_BOOKING = "reservationId:<id reserva>;newFlightId:<id nuevo vuelo>;newSeats:<número de asientos>";
    String MODIFY_BOOKING_COMMAND = "modifyReservation";

    String SHOW_ALL_CLIENTS_COMMAND = "showAllClients";

    String EXIT_OPTION = "exit";

    String MENU = String.format("""
        Please enter a command (date format is yyyy-MM-dd:HH:mm)
            -> %s %s
            -> %s %s
            -> %s %s
            -> %s %s
            -> %s %s
            -> %s %s
            -> %s (show all clients)
            -> %s (exit)
        """,
            CREATE_CLIENT_COMMAND, CREATE_CLIENT,
            CREATE_FLIGHT_COMMAND, CREATE_FLIGHT,
            CREATE_BOOKING_COMMAND, CREATE_BOOKING,
            VIEW_ITINERARY_COMMAND, VIEW_ITINERARY,
            CANCEL_BOOKING_COMMAND, CANCEL_BOOKING,
            MODIFY_BOOKING_COMMAND, MODIFY_BOOKING,
            SHOW_ALL_CLIENTS_COMMAND,
            EXIT_OPTION);

    default String getArgValue(String arg) {
        return arg.split(COLON)[1];
    }

    void execute(String command);

}
