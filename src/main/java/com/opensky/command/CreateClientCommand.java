package com.opensky.command;

public class CreateClientCommand implements Command{
    public static Command getInstance() {
        return new CreateClientCommand();
    }

    @Override
    public void execute() {

    }
}