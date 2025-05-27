package com.opensky.view;

import com.opensky.utils.Dependency;

public class ModifyBookingCommand implements Command, Dependency {

    public static ModifyBookingCommand createInstance() {
        return new ModifyBookingCommand();
    }

    @Override
    public void execute(String command) {

    }
}
