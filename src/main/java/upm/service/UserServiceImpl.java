package upm.service;

import upm.model.Player;
import upm.repo.UserRepository;
import upm.repo.UserRepositoryImpl;
import upm.utils.PlayerPrinter;

import java.util.List;

public class UserService {

    private final UserRepository userRepo;
    private final PlayerPrinter printer;

    public UserService(PlayerPrinter printer) {
        this.printer = printer;
        this.userRepo = new UserRepositoryImpl();
    }

    public void createPlayer(String name) {
        userRepo.create(new Player(name));
    }

    public void remove(String name) {
        userRepo.remove(
                userRepo.FindByUsername(name)
        );
    }

    public void findAll() {
        userRepo.findAll();
    }

    public void rank() {
        List<Player> players = userRepo.findAll();
        printer.printRank(players);
    }

    public void updateScore(String name, Double score) {
        Player player = userRepo.updateScore(name, score);
    }

}
