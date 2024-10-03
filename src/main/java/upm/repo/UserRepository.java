package upm.repo;

import upm.model.User;

import java.util.List;

public interface UserRepository {
    void create(User user);
    void remove(String username);
    List<User> findAll();
    User findByUsername(String name);
    void updateUser(User user);
    boolean existsByUsername(String s);
}
