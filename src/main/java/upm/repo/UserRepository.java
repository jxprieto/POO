package upm.repo;

import upm.model.Player;

import java.util.List;

public interface UserRepository {
    void create(Player player);
    void remove(String username);
    List<Player> findAll();
    Player findByUsername(String name);
    Player updateScore(String username, double score);
    boolean existsByUsername(String s);
}
