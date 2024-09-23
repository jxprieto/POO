package upm.utils;

import upm.model.Player;

import java.util.List;

public class PlayerPrinter implements Printer<Player>{

    public static void printMenu(){
        System.out.println("""
                Choose an option:
  
                > create [player name]
                > remove [player name]
                > show
                > rank
                > score [player name];[score]
                > show_matchmake
                > clear_matchmake
                > matchmake [player1];[player2]
                > random_matchmake
        """);
    }

    static void printEllements(List<Player> elements) {
        elements.forEach(System.out::println);
    }
}
