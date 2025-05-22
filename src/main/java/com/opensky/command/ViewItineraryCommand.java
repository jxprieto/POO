package com.opensky.command;

public class ViewItineraryCommand implements Command{
    public static ViewItineraryCommand getInstance() {
        return new ViewItineraryCommand();
    }

    @Override
    public void execute() {

    }
}
