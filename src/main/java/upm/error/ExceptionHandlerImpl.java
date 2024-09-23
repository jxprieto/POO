package upm.error;

public class ExceptionHandlerImpl implements ExceptionHandler {

    @Override
    public void handle(Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
    }

}
