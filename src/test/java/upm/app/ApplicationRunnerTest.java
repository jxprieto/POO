package upm.app;

import org.junit.jupiter.api.Test;
import upm.error.ExceptionHandler;
import upm.service.UserService;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationRunnerTest {

    private ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
    private Scanner scanner = mock(Scanner.class);
    private UserService userService = mock(UserService.class);

    @Test
    void shouldCallCreateUserWhenOptionInputIs1() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(1, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(1)).createUser();
    }

    @Test
    void shouldCallUpdateScoreWhenOptionInputIs2() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(2, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(1)).updateScore();
    }

    @Test
    void shouldCallRemoveWhenOptionInputIs3() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(3, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(1)).remove();
    }

    @Test
    void shouldCallShowWhenOptionInputIs4() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(4, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(1)).show();
    }

    @Test
    void shouldCallShowAllUsersWhenOptionInputIs5() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(5, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(1)).showAllUsers();
    }

    @Test
    void shouldCallExceptionHandlerWhenExceptionIsThrown() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenThrow(new RuntimeException());
        applicationRunner.applicationLoop();
        verify(exceptionHandler, times(1)).handle(any());
    }

    @Test
    void shouldExitWhenOptionInputIs0() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(0);
        applicationRunner.applicationLoop();
        verify(userService, never()).createUser();
        verify(userService, never()).updateScore();
        verify(userService, never()).remove();
        verify(userService, never()).show();
        verify(userService, never()).showAllUsers();
    }

    @Test
    void shouldCallDefaultOptionWhenOptionInputIsInvalid() {
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(6, 0);
        applicationRunner.applicationLoop();
        verify(userService, never()).createUser();
        verify(userService, never()).updateScore();
        verify(userService, never()).remove();
        verify(userService, never()).show();
        verify(userService, never()).showAllUsers();
    }

    @Test
    void shouldCallCorrespondingOptionsUntilInputIs0(){
        ApplicationRunner applicationRunner = new ApplicationRunner(exceptionHandler, scanner, userService);
        when(scanner.nextInt()).thenReturn(1, 2, 3, 4, 5, 1, 0);
        applicationRunner.applicationLoop();
        verify(userService, times(2)).createUser();
        verify(userService, times(1)).updateScore();
        verify(userService, times(1)).remove();
        verify(userService, times(1)).show();
        verify(userService, times(1)).showAllUsers();
    }
}