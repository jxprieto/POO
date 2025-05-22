package com.opensky.command;

public class ViewItineraryCommand implements Command, Dependency {

    public static ViewItineraryCommand getInstance() {
        return new ViewItineraryCommand();
    }

    @Override
    public void execute(String command) {

    }
}
