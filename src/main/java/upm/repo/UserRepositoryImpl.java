package upm.repo;

import upm.model.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserRepositoryImpl implements UserRepository{
    private final List<Player> players;

    public UserRepositoryImpl() {
        players = new LinkedList<>();
    }

    public void sort(){
        players.sort(
                (p1, p2) -> Double.compare(p2.getScore(), p1.getScore())
        );
    }

    @Override
    public void create(Player player) {
        players.add(player);
        sort();
    }

    @Override
    public void remove(String name) {
        players.remove(findByUsername(name));
    }

    @Override
    public List<Player> findAll() {
        return players;
    }

    @Override
    public Player findByUsername(String username) {
        return players
                .stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Player updateScore(String username, double score) {
        Player player = findByUsername(username);
        player.setScore(score);
        sort();
        return player;
    }

    @Override
    public boolean existsByUsername(String username) {
        return players
                .stream()
                .anyMatch(player -> player.getUsername().equals(username));
    }

}
