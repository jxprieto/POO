package upm.utils;

import java.util.List;

public interface Printer<T> {

    static <T> void printEllements(List<T> elements);
}
