package com.opensky.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Client {
    String id;
    String name;
    Integer age;
    String email;
    String phoneNumber;
}
