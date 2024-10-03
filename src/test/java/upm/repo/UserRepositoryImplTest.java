package upm.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.database.InMemoryDatabase;
import upm.model.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
        assertEquals(list.get(1), john);
        assertEquals(list.get(2), sara);
    }

    @Test
    void shouldRemovePlayer() {
        var john = new Player("John", "John");
        database.getList().add(john);

        userRepository.remove(john.getUsername());
        assertThat(database.getList()).isEmpty());
    }

    @Test
    void findByUsername() {
    }

    @Test
    void updateScore() {
    }
}