package upm.utils;

import java.util.List;

public interface Printer<T> {
    String MENU =
        """
            Choose an option:
      
            0 -> exit
            1 -> create player
            2 -> remove player
            3 -> show
            4 -> rank
            5 -> score [player name];[score]
            6 -> show_matchmake
            7 -> clear_matchmake
            8 -> matchmake [player1];[player2]
            9 -> random_matchmake
        """;

    static void printMenu(){
        System.out.println(MENU);
    }

    void printElements(List<T> elements);

}
