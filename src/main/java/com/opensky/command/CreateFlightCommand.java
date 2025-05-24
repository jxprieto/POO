package com.opensky.command;

import com.opensky.utils.Dependency;

public class CreateFlightCommand implements Command, Dependency {

    public static CreateFlightCommand createInstance() {
        return new CreateFlightCommand();
    }

    @Override
    public void execute(String command) {

    }
}
