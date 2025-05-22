package com.opensky.command;

public class CancelBookingCommand implements Command {
    public static CancelBookingCommand getInstance() {
        return new CancelBookingCommand();
    }

    @Override
    public void execute() {

    }
}
