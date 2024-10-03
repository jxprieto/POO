package upm.repo;

import upm.model.User;

import java.util.List;

public interface UserRepository {
    void create(final User user);
    void remove(final String username);
    List<User> findAll();
    User findByUsername(final String name);
    void updateUser(final User user);
    boolean existsByUsername(final String s);
}
