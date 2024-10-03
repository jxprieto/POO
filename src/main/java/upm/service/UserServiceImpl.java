package upm.service;

import upm.model.User;
import upm.repo.UserRepository;
import upm.utils.Printer;
import upm.utils.UserPrinter;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserServiceImpl implements UserService {

    protected static final String ENTER_USER_NAME = "Enter user name:";
    protected static final String ENTER_USER_USERNAME = "Enter user username:";
    protected static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists, please enter a different username";
    protected static final String USER_UPDATED_SUCCESSFULLY = "user updated successfully";
    protected static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";
    private static final String ENTER_USER_SURNAME = "Enter user surname:";
    private static final String USER_CREATED_SUCCESSFULLY = "User created successfully";
    public static final String USER_REMOVED_SUCCESSFULLY = "User removed successfully";


    private final UserRepository userRepo;
    private final Printer<User> printer;
    private final Scanner scanner;

    public UserServiceImpl(final UserRepository userRepo,
                           final UserPrinter printer,
                           final Scanner scanner) {
        this.userRepo = userRepo;
        this.printer = printer;
        this.scanner = scanner;
    }

    @Override
    public void createUser() {
        Printer.printMessage(ENTER_USER_NAME);
        String name = scanner.nextLine();
        Printer.printMessage(ENTER_USER_SURNAME);
        String surname = scanner.nextLine();
        Printer.printMessage(ENTER_USER_USERNAME);
        String username;
        while (userRepo.existsByUsername(username = scanner.nextLine()))
            Printer.printMessage(USERNAME_ALREADY_EXISTS_MESSAGE);
        User user = new User(name, username, surname);
        userRepo.create(user);
        Printer.printMessage(USER_CREATED_SUCCESSFULLY);
    }

    @Override
    public void remove() {
        Printer.printMessage(ENTER_USER_NAME);
        var username = scanner.nextLine();
        if (!userRepo.existsByUsername(username)) throw new NoSuchElementException(USER_NOT_FOUND_ERROR_MESSAGE);
        userRepo.remove(username);
        Printer.printMessage(USER_REMOVED_SUCCESSFULLY);
    }

    @Override
    public void showAllUsers() {
        printer.printElements(userRepo.findAll());
    }

    @Override
    public void updateScore() {
        Printer.printMessage(ENTER_USER_USERNAME);
        String username = scanner.nextLine();
        Printer.printMessage(ENTER_USER_NAME);
        String name = scanner.nextLine();
        Printer.printMessage(ENTER_USER_SURNAME);
        String surname = scanner.nextLine();

        userRepo.updateUser(new User(name, username, surname));
        Printer.printMessage(USER_UPDATED_SUCCESSFULLY);
    }

    @Override
    public void show() {
        Printer.printMessage(ENTER_USER_USERNAME);
        String username = scanner.nextLine();
        User user = userRepo.findByUsername(username);
        if (user == null) throw new NoSuchElementException(USER_NOT_FOUND_ERROR_MESSAGE);
        printer.printElement(user);
    }
}
