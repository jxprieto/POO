package upm.app;

import upm.error.ExceptionHandler;
import upm.error.ExceptionHandlerImpl;
import upm.repo.UserRepositoryImpl;
import upm.service.UserService;
import upm.service.UserServiceImpl;
import upm.utils.PlayerPrinter;
import upm.utils.Printer;

import java.io.InputStream;
import java.util.Scanner;

public class ApplicationRunner {

    public static final InputStream INPUT_STREAM = System.in;
    public static final String EXIT_MESSAGE = "Exiting...";
    public static final String INVALID_OPTION = "Invalid option";
    private final Scanner scanner;
    private final UserService userService;
    private final ExceptionHandler exceptionHandler;

    public ApplicationRunner() {
        this.exceptionHandler = new ExceptionHandlerImpl();
        this.scanner = new Scanner(INPUT_STREAM);
        this.userService = new UserServiceImpl(
                new UserRepositoryImpl(),
                new PlayerPrinter(),
                new Scanner(INPUT_STREAM)
        );
    }

    public void applicationLoop() {
        int option = 0;
        do {
            try{
                option = loopStep();
            } catch(Exception e){
                exceptionHandler.handle(e);
            }
        } while (option != 0);
    }

    private int loopStep() {
        Printer.printMenu();
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                System.out.println(EXIT_MESSAGE);
                break;
            case 1:
                userService.createPlayer();
                break;
            case 2:
                userService.remove();
                break;
            case 3:
                userService.show();
                break;
            case 4:
                userService.rank();
                break;
            case 5:
                userService.updateScore();
                break;
            case 6:
                userService.showMatchMake();
                break;
            case 7:
                userService.clearMatchMake();
                break;
            case 8:
                userService.matchMake();
                break;
            case 9:
                userService.randomMatchMake();
                break;
            default:
                System.out.println(INVALID_OPTION);
        }
        return option;
    }
}
