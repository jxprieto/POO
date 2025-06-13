package com.opensky.view;

import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class DefaultViewCommand implements Command, Dependency {
    public static final DependencyInjector di = DependencyInjector.getDefaultImplementation();
    private static final String INVALID_COMMAND_MESSAGE = "Invalid command. Please try again.";

    public DefaultViewCommand(Printer printer) {
        this.printer = printer;
    }

    public static DefaultViewCommand createInstance(){
        return new DefaultViewCommand(di.getDependency(ConsolePrinter.class));
    }

    private final Printer printer;

    @Override
    public void execute(String command) {
        printer.print(INVALID_COMMAND_MESSAGE + "\n");
    }
}
