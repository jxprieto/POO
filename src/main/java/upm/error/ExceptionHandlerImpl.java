package upm.error;

import upm.utils.Printer;

public class ExceptionHandlerImpl implements ExceptionHandler {

    @Override
    public void handle(final Exception e) {
        Printer.printMessage("An error occurred: " + e.getMessage());
    }

}
