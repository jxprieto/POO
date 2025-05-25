package com.opensky.printer;

import com.opensky.utils.Dependency;

public class ConsolePrinter implements Printer, Dependency {

    public static ConsolePrinter createInstance(){
        return new ConsolePrinter();
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
