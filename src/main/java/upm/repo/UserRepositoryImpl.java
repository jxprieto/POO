package upm.repo;

import upm.database.InMemoryDatabase;
import upm.model.User;

import java.util.List;
import java.util.NoSuchElementException;

public class UserRepositoryImpl implements UserRepository{

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";

    private final InMemoryDatabase<User> database;

    public UserRepositoryImpl(final InMemoryDatabase<User> database) {
         this.database = database;
    }

    @Override
    public void create(final User user) {
        database.getList().add(user);
    }

    @Override
    public void remove(final String username) {
        database.getList().remove(findByUsername(username));
    }

    @Override
    public List<User> findAll() {
        return database.getList();
    }

    @Override
    public User findByUsername(final String username) {
        return database.getList()
                .stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateUser(final User user) {
        final User oldUser = findByUsername(user.getUsername());
        if (oldUser == null) throw new NoSuchElementException(USER_NOT_FOUND_ERROR_MESSAGE);
        remove(user.getUsername());
        try{
            create(user);
        }catch (Exception e){
            create(oldUser);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByUsername(final String username) {
        return database.getList()
                .stream()
                .anyMatch(player -> player.getUsername().equalsIgnoreCase(username));
    }

}
