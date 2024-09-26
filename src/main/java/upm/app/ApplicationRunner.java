package upm.app;

import upm.error.ExceptionHandler;
import upm.error.ExceptionHandlerImpl;
import upm.repo.UserRepositoryImpl;
import upm.service.MatchService;
import upm.service.MatchServiceImpl;
import upm.service.UserService;
import upm.service.UserServiceImpl;
import upm.utils.PlayerPrinter;
import upm.utils.Printer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApplicationRunner {

    public static final InputStream INPUT_STREAM = System.in;
    public static final String EXIT_MESSAGE = "Exiting...";
    public static final String INVALID_OPTION = "Invalid option";
    public static final Runnable DEFAULT_OPTION = () -> System.out.println(INVALID_OPTION);
    public final Map<Integer, Runnable> ACTIONS_BY_OPTION = new HashMap<>();
    private final Scanner scanner;
    private final UserService userService;
    private final MatchService matchService;
    private final ExceptionHandler exceptionHandler;


    public ApplicationRunner() {
        // todo refactor to use dependency injection using singleton pattern
        this.matchService = new MatchServiceImpl();
        this.exceptionHandler = new ExceptionHandlerImpl();
        this.scanner = new Scanner(INPUT_STREAM);
        this.userService = new UserServiceImpl(
                new UserRepositoryImpl(),
                new PlayerPrinter(),
                new Scanner(INPUT_STREAM)
        );
        mapActions();
    }

    private void mapActions() {
        ACTIONS_BY_OPTION.put(0, () -> System.out.println(EXIT_MESSAGE));
        ACTIONS_BY_OPTION.put(1, userService::createPlayer);
        ACTIONS_BY_OPTION.put(2, userService::remove);
        ACTIONS_BY_OPTION.put(3, userService::show);
        ACTIONS_BY_OPTION.put(4, userService::rank);
        ACTIONS_BY_OPTION.put(5, userService::updateScore);
        ACTIONS_BY_OPTION.put(6, matchService::showMatchMake);
        ACTIONS_BY_OPTION.put(7, matchService::clearMatchMake);
        ACTIONS_BY_OPTION.put(8, matchService::matchMake);
        ACTIONS_BY_OPTION.put(9, matchService::randomMatchMake);
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
        ACTIONS_BY_OPTION
                .getOrDefault(option, DEFAULT_OPTION)
                .run();
        return option;
    }
}
