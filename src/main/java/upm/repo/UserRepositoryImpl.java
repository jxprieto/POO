package upm.repo;

import upm.database.InMemoryDatabase;
import upm.model.Player;

import java.util.List;
import java.util.NoSuchElementException;

public class UserRepositoryImpl implements UserRepository{

    private final InMemoryDatabase<Player> database;
    public UserRepositoryImpl(InMemoryDatabase<Player> database) {
         this.database = database;
    }

    public void sort(){
        database.getList().sort(
                (p1, p2) -> Double.compare(p2.getScore(), p1.getScore())
        );
    }

    @Override
    public void create(Player player) {
        database.getList().add(player);
        sort();
    }

    @Override
    public void remove(String username) {
        database.getList().remove(findByUsername(username));
    }

    @Override
    public List<Player> findAll() {
        return database.getList();
    }

    @Override
    public Player findByUsername(String username) {
        return database.getList()
                .stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
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
        return database.getList()
                .stream()
                .anyMatch(player -> player.getUsername().equalsIgnoreCase(username));
    }

}
