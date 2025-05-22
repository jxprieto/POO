package com.opensky.command;

public interface Dependency{
    /**
     * This method is used to create an instance of the class that implements this interface.
     * It is expected to be overridden in the implementing class.
     *
     * @return an instance of the implementing class
     */
    static Object createInstance(){
        throw new UnsupportedOperationException("Only supported in subclasses");
    }
}
