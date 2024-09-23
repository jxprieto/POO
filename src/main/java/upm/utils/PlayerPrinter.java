package upm.utils;

import upm.model.Player;

import java.util.List;

public class PlayerPrinter implements Printer<Player>{

    @Override
    public void printElements(List<Player> elements) {
        elements.forEach(System.out::println);
    }
}
