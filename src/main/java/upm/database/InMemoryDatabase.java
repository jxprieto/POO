package upm.database;

import upm.model.Player;

import java.util.LinkedList;
import java.util.List;

public class InMemoryDatabase<T> {
    private final List<T> elements;

    public InMemoryDatabase() {
        this.elements = new LinkedList<>();
    }

    public List<T> getList() {
        return elements;
    }
}
