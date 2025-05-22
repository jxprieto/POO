package com.opensky.command;

public class CreateFlightCommand implements Command{
    public static CreateFlightCommand getInstance() {
        return new CreateFlightCommand();
    }

    @Override
    public void execute() {

    }
}
