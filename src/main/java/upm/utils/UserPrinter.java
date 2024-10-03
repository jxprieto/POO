package upm.utils;

import upm.model.User;

import java.io.PrintStream;
import java.util.List;

public class UserPrinter implements Printer<User>{

    private final PrintStream out;

    public UserPrinter(final PrintStream out) {
        this.out = out;
    }

    @Override
    public void printElements(final List<User> elements){ //todo manage exception as unexpected
        elements.forEach(out::println);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printElement(final User user) {
        out.println(user);
    }
}
