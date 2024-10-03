package upm.service;

import upm.model.Player;
import upm.repo.UserRepository;
import upm.utils.PlayerPrinter;
import upm.utils.Printer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserServiceImpl implements UserService {

    public static final String ENTER_PLAYER_NAME = "Enter player name:";
    private static final String ENTER_PLAYER_SCORE = "Enter player score:";
    private static final String ENTER_PLAYER_USERNAME = "Enter player username:";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists, please enter a different username";
    public static final String SCORE_UPDATED_SUCCESSFULLY = "Score updated successfully";
    public static final String PLAYER_NOT_FOUND_ERROR_MESSAGE = "Player not found";


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
        System.out.println(ENTER_PLAYER_NAME);
        String name = scanner.nextLine();
        System.out.println(ENTER_PLAYER_USERNAME);
        String username;
        while (userRepo.existsByUsername(username = scanner.nextLine()))
            System.out.println(USERNAME_ALREADY_EXISTS_MESSAGE);
        Player player = new Player(name, username);
        userRepo.create(player);
    }

    @Override
    public void remove() {
        System.out.println(ENTER_PLAYER_NAME);
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
        System.out.println(ENTER_PLAYER_NAME);
        String name = scanner.nextLine();
        System.out.println(ENTER_PLAYER_SCORE);
        double score;
        score = Double.parseDouble(scanner.nextLine()); // todo throws number format exception, manage in exception handler
        userRepo.updateScore(name, score);
        Printer.printMessage(SCORE_UPDATED_SUCCESSFULLY);
    }

    @Override
    public void show() {
        System.out.println(ENTER_PLAYER_NAME);
        String name = scanner.nextLine();
        Player player = userRepo.findByUsername(name);
        printer.printElement(player);
    }
}
