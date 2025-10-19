package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Scanner;

public final class CreateUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUser.class);
    private final UserService userService;

    public CreateUser(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService must not be null");
        }
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        if (scanner == null) {
            LOGGER.error("Scanner must not be null");
            return;
        }

        LOGGER.info("Starting to create a new user...");

        User user = new User();

        try {
            LOGGER.info("Enter name:");
            user.setName(InputValidator.validateNameForCreate(scanner));

            LOGGER.info("Enter email:");
            user.setEmail(InputValidator.validateEmailForCreate(scanner));

            LOGGER.info("Enter age:");
            user.setAge(InputValidator.validateAgeForCreate(scanner));

            Long id = userService.create(user);
            if (id == null || id < 0) {
                LOGGER.error("Failed to create user");
            } else {
                LOGGER.info("Created user with ID: {}", id);
                LOGGER.info("Name: {}, Email: {}, Age: {}", user.getName(), user.getEmail(), user.getAge());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error during user creation: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error during user creation: ", e);
        }
    }
}
