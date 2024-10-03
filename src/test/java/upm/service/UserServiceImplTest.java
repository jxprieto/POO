package upm.service;

import org.junit.jupiter.api.Test;
import upm.repo.UserRepository;
import upm.utils.PlayerPrinter;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PlayerPrinter playerPrinter = mock(PlayerPrinter.class);
    private final Scanner scanner = mock(Scanner.class);

    private final UserService userService = new UserServiceImpl(userRepository, playerPrinter, scanner);

    @Test
    void shouldAskForNameAndUsernameAndCallRepository() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(scanner.nextLine()).thenReturn("name", "username");
        userService.createPlayer();
        verify(scanner, times(2)).nextLine();
        verify(userRepository).create(any());
    }
    @Test
    void shouldAskForUsernameUntilItDoesntExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true, true, true, false);
        when(scanner.nextLine()).thenReturn("name", "username");
        userService.createPlayer();
        verify(scanner, times(5)).nextLine();
        verify(userRepository).create(any());
    }

    @Test
    void remove() {
    }

    @Test
    void rank() {
    }

    @Test
    void updateScore() {
    }

    @Test
    void show() {
    }
}