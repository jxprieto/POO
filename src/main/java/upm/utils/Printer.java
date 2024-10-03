package upm.utils;

import upm.model.Player;

import java.util.List;

public interface Printer<T> {
    String MENU =
        """
            Choose an option:
      
            0 -> exit
            1 -> create player
            2 -> update player
            3 -> remove player
            4 -> show user
            5 -> rank users by score
            
            Write the number of the option you want to choose:
        """;

    static void printMenu(){
        System.out.print(MENU);
    }

    static void printMessage(String message) {
        System.out.println(message);
    }

    void printElements(List<T> elements);

    void printElement(Player player);
}
