package com.opensky.command;

public class ModifyBookingCommand implements Command, Dependency {

    public static ModifyBookingCommand getInstance() {
        return new ModifyBookingCommand();
    }

    @Override
    public void execute(String command) {

    }
}
