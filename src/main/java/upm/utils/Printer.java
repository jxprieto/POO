package upm.utils;

import upm.model.User;

import java.util.List;

public interface Printer<T> {
    final String MENU =
        """
            Choose an option:
      
            0 -> exit
            1 -> create user
            2 -> update user
            3 -> remove user
            4 -> show user
            5 -> rank all users
            
            Write the number of the option you want to choose:
        """;

    static void printMenu(){
        System.out.print(MENU);
    }

    static void printMessage(final String message) {
        System.out.println(message);
    }

    void printElements(final List<T> elements);

    void printElement(final User user);
}
