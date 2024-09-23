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
    public Player findByUsername(String name) {
        return players
                .stream()
                .filter(player -> player.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Player updateScore(String name, double score) {
        Player player = findByUsername(name);
        player.setScore(score);
        sort();
        return player;
    }

}
