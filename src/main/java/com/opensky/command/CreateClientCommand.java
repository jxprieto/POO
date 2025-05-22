package com.opensky.command;

public class CreateClientCommand implements Command, Dependency {

    public static CreateClientCommand createInstance() {
        return new CreateClientCommand();
    }

    @Override
    public void execute(String command) {

    }
}