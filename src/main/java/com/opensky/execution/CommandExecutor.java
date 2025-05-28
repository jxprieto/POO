package com.opensky.execution;

import com.opensky.exception.OpenSkyException;
import com.opensky.printer.ConsolePrinter;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;
import com.opensky.view.Command;

public class CommandExecutor implements Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static CommandExecutor createInstance() {
        return new CommandExecutor(
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final ConsolePrinter printer;

    public CommandExecutor(ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
    }

    public void executeCommand(Command command, String arguments) {
        try{
            command.execute(arguments);
        }catch (OpenSkyException e) {
            printer.print("ERROR: " + e.getMessage());
        } catch (Exception e) {
            printer.print("Unexpected internal error: " + e.getMessage());
        }
    }
}
