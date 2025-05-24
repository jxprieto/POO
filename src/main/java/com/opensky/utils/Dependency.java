package com.opensky.utils;

public interface Dependency{
    /**
     * This method is used to create an instance of the class that implements this interface.
     * It is expected to be overridden in the implementing class.
     *
     * @return an instance of the implementing class
     */
    static Dependency createInstance(){
        throw new UnsupportedOperationException("Only supported in subclasses. All subclasses must override this method.");
    }
}
