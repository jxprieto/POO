package com.opensky.command;

import com.opensky.utils.Dependency;

public class CancelBookingCommand implements Command, Dependency {

    @Override
    public void execute(String command) {

    }

    public static CancelBookingCommand createInstance() {
        return new CancelBookingCommand();
    }
}
