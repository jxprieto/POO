package upm.utils;

import upm.model.Player;

import java.rmi.UnexpectedException;
import java.util.List;

public class PlayerPrinter implements Printer<Player>{

    @Override
    public void printElements(List<Player> elements){ //todo manage exception as unexpected
        elements.forEach(System.out::println);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printElement(Player player) {
        System.out.println(player);
    }
}
