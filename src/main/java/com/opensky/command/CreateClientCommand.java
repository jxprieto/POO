package com.opensky.command;

import com.opensky.utils.Dependency;

public class CreateClientCommand implements Command, Dependency {

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand();
    }

    @Override
    public void execute(String command) {

    }
}