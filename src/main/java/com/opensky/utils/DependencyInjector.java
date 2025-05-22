package com.opensky.utils;

import com.opensky.command.Dependency;

public interface DependencyInjector {
    public <T extends Dependency> T getDependency(Class<T> clazz);
}
