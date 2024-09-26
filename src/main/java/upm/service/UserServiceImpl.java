package upm.service;

import upm.model.Player;
import upm.repo.UserRepository;
import upm.utils.PlayerPrinter;
import upm.utils.Printer;

import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements UserService {

    public static final String ENTER_PLAYER_NAME = "Enter player name:";
    private static final String ENTER_PLAYER_SCORE = "Enter player score:";
    private final UserRepository userRepo;
    private final Printer printer;
    private final Scanner scanner;

    public UserServiceImpl(UserRepository userRepo, PlayerPrinter printer, Scanner scanner) {
        this.userRepo = userRepo;
        this.printer = printer;
        this.scanner = scanner;
    }

    @Override
    public void createPlayer() {
        System.out.println(ENTER_PLAYER_NAME);
        Player player = new Player(scanner.nextLine());
        userRepo.create(player);
    }

    @Override
    public void remove() {
        System.out.println(ENTER_PLAYER_NAME);
        userRepo.remove(scanner.nextLine());
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
        score = Double.parseDouble(scanner.nextLine());// throws number format exception
        userRepo.updateScore(name, score);
    }

    @Override
    public void show() {

    }
}
