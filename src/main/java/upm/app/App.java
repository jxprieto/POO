package upm.app;

import upm.database.InMemoryDatabase;
import upm.error.ExceptionHandler;
import upm.error.ExceptionHandlerImpl;
import upm.repo.UserRepositoryImpl;
import upm.service.UserService;
import upm.service.UserServiceImpl;
import upm.utils.UserPrinter;

import java.io.InputStream;
import java.util.Scanner;

public class App {

    public static final InputStream INPUT_STREAM = System.in;

    public static void main(String[] args) {
        //todo refactor to use dependency injection with singleton pattern
        ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();
        Scanner scanner = new Scanner(INPUT_STREAM);
        UserService userService = new UserServiceImpl(
                new UserRepositoryImpl(new InMemoryDatabase<>()),
                new UserPrinter(System.out),
                new Scanner(INPUT_STREAM)
        );
        final ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        applicationRunner.applicationLoop();
    }

}
