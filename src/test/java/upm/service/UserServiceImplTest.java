package upm.service;

import org.junit.jupiter.api.Test;
import upm.model.User;
import upm.repo.UserRepository;
import upm.utils.UserPrinter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
class UserServiceImplTest {

    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    private final User user = User.builder().withName(NAME).withSurname(SURNAME).withUsername(USERNAME).build();


    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserPrinter userPrinter = mock(UserPrinter.class);
    private final Scanner scanner = mock(Scanner.class);

    private final UserService userService = new UserServiceImpl(userRepository, userPrinter, scanner);

    @Test
    void shouldAskForNameAndUsernameAndCallRepository() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(scanner.nextLine()).thenReturn(NAME, USERNAME);
        userService.createUser();
        verify(scanner, times(3)).nextLine();
        verify(userRepository).create(any());
    }
    @Test
    void shouldAskForUsernameUntilItDoesntExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true,true, true, false);
        when(scanner.nextLine()).thenReturn(NAME, USERNAME);
        userService.createUser();
        verify(scanner, times(6)).nextLine();
        verify(userRepository).create(any());
    }

    @Test
    void shouldRemove() {
        when(scanner.nextLine()).thenReturn(USERNAME);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        userService.remove();
        verify(scanner).nextLine();
        verify(userRepository).remove(USERNAME);
    }
    @Test
    void shouldThrowExceptionWhenUsernameDoesNotExists() {
        when(scanner.nextLine()).thenReturn(USERNAME);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        Exception e = assertThrows(NoSuchElementException.class, () -> userService.remove());
        assertThat(e.getMessage(), equalTo("User not found"));
        assertThat(e, instanceOf(NoSuchElementException.class));
    }

    @Test
    void showAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        userService.showAllUsers();
        verify(userPrinter).printElements(List.of(user));
    }

    @Test
    void updateScore() {
        when(scanner.nextLine()).thenReturn(USERNAME, NAME, SURNAME);
        userService.updateScore();
        verify(scanner, times(3)).nextLine();
        verify(userRepository).updateUser(any());
    }

    @Test
    void show() {
        when(scanner.nextLine()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        userService.show();
        verify(scanner).nextLine();
        verify(userPrinter).printElement(user);
    }
}