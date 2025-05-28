package com.opensky.view;

import com.opensky.utils.Dependency;

public class CancelBookingViewCommand implements Command, Dependency {

    public static CancelBookingViewCommand createInstance() {
        return new CancelBookingViewCommand();
    }

    private CancelBookingViewCommand() {}

    @Override
    public void execute(String command) {

    }
}
