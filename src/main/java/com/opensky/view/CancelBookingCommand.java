package com.opensky.view;

import com.opensky.utils.Dependency;

public class CancelBookingCommand implements Command, Dependency {

    public static CancelBookingCommand createInstance() {
        return new CancelBookingCommand();
    }

    @Override
    public void execute(String command) {

    }
}
