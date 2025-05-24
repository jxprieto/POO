package com.opensky.utils;

public interface DependencyInjector {

    static DependencyInjector getDefaultImplementation() {
        return SingletonDependencyInjector.getInstance();
    }

    <T extends Dependency> T getDependency(Class<T> clazz);
}
