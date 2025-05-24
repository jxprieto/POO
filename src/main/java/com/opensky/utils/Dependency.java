package com.opensky.utils;

public interface Dependency{
    /**
     * This method is used to create an instance of the class that implements this interface.
     * It is expected to be overridden in the implementing class.
     *
     * @return an instance of the implementing class
     */
    static Dependency createInstance(){
        //todo create an annotation to ensure that subclasses overrides just methods with the annotation,
        // and not all static methods
        // annotation @Override is not allowed in static methods, so we cannot use it here.
        // annotation name could be @MustOverrideStaticMethod and in subclasses we might use @StaticOverride
        throw new UnsupportedOperationException("Only supported in subclasses. All subclasses must override this method.");
    }
}
