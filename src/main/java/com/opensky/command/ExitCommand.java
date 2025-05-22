package com.opensky.command;

public class ExitCommand implements Command{
    public static ExitCommand getInstance() {
        return new ExitCommand();
    }

    @Override
    public void execute() {
        System.out.println("Exiting the application...");
    }
}
