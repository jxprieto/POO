package com.opensky.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder(toBuilder = true)
public class Client extends Entity<String> {
    String name;
    Integer age;
    String email;
    String phoneNumber;
}
