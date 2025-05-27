package com.opensky.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
public abstract class Entity<T> {
    T id;
}
