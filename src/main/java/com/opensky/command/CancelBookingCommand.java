package com.opensky.command;

public class CancelBookingCommand implements Command, Dependency {

    public static CancelBookingCommand getInstance() {
        return new CancelBookingCommand();
    }

    @Override
    public void execute(String command) {

    }

    public static CancelBookingCommand createInstance() {
        return new CancelBookingCommand();
    }
}
