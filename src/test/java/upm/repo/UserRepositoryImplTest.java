package upm.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.database.InMemoryDatabase;
import upm.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;
    private final InMemoryDatabase<User> database = new InMemoryDatabase<>();

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(database);
    }

    @Test
    void shouldAddElements() {
        var user = new User("John", "john", "john");
        userRepository.create(user);
        assertEquals(database.getList().get(0), user);
    }

    @Test
    void shouldRemovePlayer() {
        var john = new User("john", "john", "john");
        database.getList().add(john);

        userRepository.remove(john.getUsername());
        assertTrue(database.getList().isEmpty());
    }

    @Test
    void shouldFindByUsername() {
        var john = new User("john", "john", "john");
        database.getList().add(john);

        var player = userRepository.findByUsername(john.getUsername());
        assertEquals(player, john);
    }
    @Test
    void shouldIgnoreCaseWhenFindByUsername() {
        var john = new User("john", "john", "john");
        database.getList().add(john);

        var player = userRepository.findByUsername("JOHN");
        assertEquals(player, john);
    }

    @Test
    void shouldUpdate() {
        var john = new User("john", "john", "john");
        List<User> list = database.getList();
        list.add(john);

        User updated = new User("new", "john", "new");
        userRepository.updateUser(updated);

        assertThat(list.get(0), equalTo(updated));
        assertThat(list.size(), equalTo(1));
    }

    @Test
    void shouldReturnTrueIfPlayerExistsWhenCallExistsByUsername() {
        var john = new User("john", "john", "john");
        List<User> list = database.getList();
        list.add(john);

        assertTrue(userRepository.existsByUsername(john.getUsername()));
    }
    @Test
    void shouldReturnFalseIfPlayerDoesntExistsWhenCallExistsByUsername() {
        var john = new User("john", "john", "john");
        List<User> list = database.getList();
        list.add(john);

        assertFalse(userRepository.existsByUsername("sara"));
    }
    @Test
    void shouldIgnoreCaseWhenCallExistsByUsername() {
        var john = new User("john", "john", "john");
        List<User> list = database.getList();
        list.add(john);

        assertTrue(userRepository.existsByUsername("JOHN"));
    }

    @Test
    void shouldFindAll() {
        var john = new User("John", "John", "john");
        var sara = new User("Sara", "Sara", "sara");
        var lionel = new User("Lionel", "Lionel", "lionel");

        List list = database.getList();
        list.add(john);
        list.add(sara);
        list.add(lionel);

        assertEquals(userRepository.findAll(), list);
    }
}