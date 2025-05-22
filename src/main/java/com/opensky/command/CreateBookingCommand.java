package com.opensky.command;

public class CreateBookingCommand implements Command{
    public static CreateBookingCommand getInstance() {
        return new CreateBookingCommand();
    }

    @Override
    public void execute() {

    }
}
