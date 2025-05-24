package com.opensky.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public abstract class Entity {
    String id;
}
