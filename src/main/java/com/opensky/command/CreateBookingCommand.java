package com.opensky.command;

public class CreateBookingCommand implements Command, Dependency {

    public static CreateBookingCommand createInstance() {
        return new CreateBookingCommand();
    }

    @Override
    public void execute(String command) {

    }

}
