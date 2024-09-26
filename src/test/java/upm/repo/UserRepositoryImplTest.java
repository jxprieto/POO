package upm.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.model.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void shouldAddElementsCorrectly() {
        userRepository.create(new Player("John", 10));
        userRepository.findByUsername("sara");
    }


    @Test
    void findAll() {

    }

    @Test
    void allElementsShouldBeSortedAfterCreatingThem() {

        var john = new Player("John", 10);
        var sara = new Player("Sara", 2);
        var lionel = new Player("Lionel", 100);
        userRepository.create(john);
        userRepository.create(sara);
        userRepository.create(lionel);

        var list = userRepository.findAll();
        assertEquals(list.get(0), lionel);
        assertEquals(list.get(1), john);
        assertEquals(list.get(2), sara);
    }

    @Test
    void remove() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void updateScore() {
    }
}