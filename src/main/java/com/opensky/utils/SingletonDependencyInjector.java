package com.opensky.utils;

import com.opensky.command.Dependency;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;

public class SingletonDependencyInjector implements DependencyInjector {

    private static SingletonDependencyInjector instance;
    private final Map<Class<? extends Dependency>,  Dependency> dependencies = new HashMap<>();

    private SingletonDependencyInjector() {}

    public static SingletonDependencyInjector getInstance() {
       return instance != null ? instance : (instance = new SingletonDependencyInjector());
    }

    @Override
    public <T extends Dependency> T getDependency(Class<T> clazz) {
        if (!Dependency.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException(clazz.getName() + " does not implement Dependency");
        Dependency existing = dependencies.get(clazz);
        if (existing != null) return clazz.cast(existing);
        try {
            final Object response = clazz.getMethod("createInstance").invoke(null);
            if (!(response instanceof Dependency dependency))
                throw new IllegalStateException("Returned object does not implement Dependency");
            dependencies.put(clazz, dependency);
            return clazz.cast(dependency);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate dependency: " + clazz.getName(), e);
        }
    }

}
