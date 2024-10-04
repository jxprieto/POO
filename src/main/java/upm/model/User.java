package upm.model;

public class User {

    private final String surname;
    private final String name;
    private final String username;

    public User(final String name, final String username, final String surname) {
        this.name = name;
        this.username = username;
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public static final class UserBuilder {
        private String name;
        private String surname;
        private String username;

        public UserBuilder() {
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
