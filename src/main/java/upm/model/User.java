package upm.model;

import java.time.LocalDate;

public class User {
    private final String name;
    private final String surname;
    private final String username;

    public User(String name, String username, String surname) {
        this.name = name;
        this.username = username;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private String name;
        private String surname;
        private String username;

        public UserBuilder() {
        }

        public UserBuilder(User other) {
            this.name = other.name;
            this.surname = other.surname;
            this.username = other.username;
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public User build() {
            return new User(name, username, surname);
        }
    }
}
