package com.opensky.utils;

public interface DependencyInjector {
    <T extends Dependency> T getDependency(Class<T> clazz);
}
