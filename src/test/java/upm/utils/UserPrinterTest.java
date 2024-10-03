package upm.utils;

import org.junit.jupiter.api.Test;
import upm.model.User;

import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserPrinterTest {

    public static final User USER = new User("name", "username", "surname");
    public static final List<User> USERS = List.of(USER);
    private final PrintStream out = mock(PrintStream.class);
    private final UserPrinter userPrinter = new UserPrinter(out);


    @Test
    void shouldPrintElements() {
        userPrinter.printElements(USERS);
        verify(out).println(USERS.get(0));
    }

    @Test
    void printElement() {
        userPrinter.printElement(USER);
        verify(out).println(USER);
    }
}