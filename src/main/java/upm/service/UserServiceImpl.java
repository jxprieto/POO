package upm.service;

import upm.model.Player;
import upm.repo.UserRepository;
import upm.utils.PlayerPrinter;
import upm.utils.Printer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserServiceImpl implements UserService {

    protected static final String ENTER_PLAYER_NAME = "Enter player name:";
    protected static final String ENTER_PLAYER_SCORE = "Enter player score:";
    protected static final String ENTER_PLAYER_USERNAME = "Enter player username:";
    protected static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists, please enter a different username";
    protected static final String SCORE_UPDATED_SUCCESSFULLY = "Score updated successfully";
    protected static final String PLAYER_NOT_FOUND_ERROR_MESSAGE = "Player not found";


    private final UserRepository userRepo;
    private final Printer<Player> printer;
    private final Scanner scanner;

    public UserServiceImpl(UserRepository userRepo, PlayerPrinter printer, Scanner scanner) {
        this.userRepo = userRepo;
        this.printer = printer;
        this.scanner = scanner;
    }

    @Override
    public void createPlayer() {
        Printer.printMessage(ENTER_PLAYER_NAME);
        String name = scanner.nextLine();
        Printer.printMessage(ENTER_PLAYER_USERNAME);
        String username;
        while (userRepo.existsByUsername(username = scanner.nextLine()))
            Printer.printMessage(USERNAME_ALREADY_EXISTS_MESSAGE);
        Player player = new Player(name, username);
        userRepo.create(player);
    }

    @Override
    public void remove() {
        Printer.printMessage(ENTER_PLAYER_NAME);
        var username = scanner.nextLine();
        if (userRepo.findByUsername(username) == null) throw new NoSuchElementException(PLAYER_NOT_FOUND_ERROR_MESSAGE);
        userRepo.remove(username);
    }

    @Override
    public void rank() {
        List<Player> players = userRepo.findAll();
        printer.printElements(players);
    }

    @Override
    public void updateScore() {
        Printer.printMessage(ENTER_PLAYER_USERNAME);
        String username = scanner.nextLine();
        Printer.printMessage(ENTER_PLAYER_SCORE);
        double score;
        score = Double.parseDouble(scanner.nextLine()); // todo throws number format exception, manage in exception handler
        userRepo.updateScore(username, score);
        Printer.printMessage(SCORE_UPDATED_SUCCESSFULLY);
    }

    @Override
    public void show() {
        Printer.printMessage(ENTER_PLAYER_USERNAME);
        String username = scanner.nextLine();
        Player player = userRepo.findByUsername(username);
        printer.printElement(player);
    }
}
