package upm.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.database.InMemoryDatabase;
import upm.model.Player;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;
    private final InMemoryDatabase<Player> database = new InMemoryDatabase<>();

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(database);
    }

    @Test
    void shouldAddElementsCorrectly() {
        var user = new Player("John", "john");
        userRepository.create(user);
        assertEquals(database.getList().get(0), user);
    }

    @Test
    void allElementsShouldBeSortedAfterUpdatingThem() {
        var john = new Player("John", "John");
        var sara = new Player("Sara", "Sara");
        var lionel = new Player("Lionel", "Lionel");

        userRepository.create(john);
        userRepository.create(sara);
        userRepository.create(lionel);
        userRepository.updateScore(sara.getUsername(), 10);
        userRepository.updateScore(lionel.getUsername(), 5);

        var list = database.getList();
        assertEquals(list.get(0), sara);
        assertEquals(list.get(1), lionel);
        assertEquals(list.get(2), john);
    }

    @Test
    void shouldRemovePlayer() {
        var john = new Player("john", "john");
        database.getList().add(john);

        userRepository.remove(john.getUsername());
        assertTrue(database.getList().isEmpty());
    }

    @Test
    void shouldFindByUsername() {
        var john = new Player("john", "john");
        database.getList().add(john);

        var player = userRepository.findByUsername(john.getUsername());
        assertEquals(player, john);
    }
    @Test
    void shouldIgnoreCaseWhenFindByUsername() {
        var john = new Player("john", "john");
        database.getList().add(john);

        var player = userRepository.findByUsername("JOHN");
        assertEquals(player, john);
    }


    @Test
    void shouldUpdate() {
        var john = new Player("john", "john");
        List<Player> list = database.getList();
        list.add(john);

        userRepository.updateScore(john.getUsername(), 10);

        assertEquals(list.get(0).getScore(), 10);
        assertThat(list.size(), equalTo(1));
    }

    @Test
    void shouldReturnTrueIfPlayerExistsWhenCallExistsByUsername() {
        var john = new Player("john", "john");
        List<Player> list = database.getList();
        list.add(john);

        assertTrue(userRepository.existsByUsername(john.getUsername()));
    }
    @Test
    void shouldReturnFalseIfPlayerDoesntExistsWhenCallExistsByUsername() {
        var john = new Player("john", "john");
        List<Player> list = database.getList();
        list.add(john);

        assertFalse(userRepository.existsByUsername("sara"));
    }
    @Test
    void shouldIgnoreCaseWhenCallExistsByUsername() {
        var john = new Player("john", "john");
        List<Player> list = database.getList();
        list.add(john);

        assertTrue(userRepository.existsByUsername("JOHN"));
    }

    @Test
    void shouldFindAll() {
        var john = new Player("John", "John");
        var sara = new Player("Sara", "Sara");
        var lionel = new Player("Lionel", "Lionel");

        List list = database.getList();
        list.add(john);
        list.add(sara);
        list.add(lionel);

        assertEquals(userRepository.findAll(), list);
    }
}