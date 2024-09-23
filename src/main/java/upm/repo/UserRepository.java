package upm.repo;

import upm.model.Player;

import java.util.List;

public interface UserRepository {
    void create(Player player);
    void remove(String name);
    List<Player> findAll();
    Player findByUsername(String name);

    Player updateScore(String name, double score);
}
