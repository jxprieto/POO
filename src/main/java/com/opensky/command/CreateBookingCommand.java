package com.opensky.command;

import com.opensky.utils.Dependency;

public class CreateBookingCommand implements Command, Dependency {

    public static CreateBookingCommand createInstance() {
        return new CreateBookingCommand();
    }

    @Override
    public void execute(String command) {

    }

}
