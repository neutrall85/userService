package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CreateUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUser.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private final UserService userService;

    public CreateUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("\033[33mEnter name: \033[0m");
        String name = scanner.nextLine();

        String email = null;
        boolean isValidEmail = false;
        while (!isValidEmail) {
            LOGGER.info("\033[33mEnter email: \033[0m");
            email = scanner.nextLine();

            if (isValidEmail(email)) {
                isValidEmail = true;
            } else {
                LOGGER.warn("\033[31mInvalid email format. Please try again.\033[0m");
            }
        }

        LOGGER.info("\033[33mEnter age: \033[0m");
        int age = scanner.nextInt();
        scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        Long id = userService.create(user);
        LOGGER.info("\033[32mCreated user with ID: {}\033[0m", id);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
