package com.opensky.view;

import com.opensky.utils.Dependency;

public class ViewItineraryCommand implements Command, Dependency {

    public static ViewItineraryCommand createInstance() {
        return new ViewItineraryCommand();
    }

    @Override
    public void execute(String command) {

    }
}
