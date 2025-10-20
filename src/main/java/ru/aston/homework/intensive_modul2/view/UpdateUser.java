package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Optional;
import java.util.Scanner;

public final class UpdateUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUser.class);
    private final UserService userService;

    public UpdateUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        try {
            LOGGER.info("Enter user ID to update: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isEmpty()) {
                LOGGER.warn("User not found!");
                return;
            }

            User user = userOptional.get();
            displayCurrentUserData(user);
            updateUserFields(user, scanner);

            userService.update(user);
            LOGGER.info("User updated successfully!");
            verifyUpdate(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid user ID format: ", e);
        } catch (Exception e) {
            LOGGER.error("Error during user update: ", e);
        }
    }

    public void displayCurrentUserData(User user) {
        LOGGER.info("Current user data:");
        LOGGER.info("Name: {}, Email: {}, Age: {}", user.getName(), user.getEmail(), user.getAge());
    }

    void updateUserFields(User user, Scanner scanner) {
        updateName(user, scanner);
        updateEmail(user, scanner);
        updateAge(user, scanner);
    }

    public void updateName(User user, Scanner scanner) {
        LOGGER.info("Enter new name (leave blank to keep current): ");
        String currentName = user.getName();
        String name = InputValidator.validateName(scanner, currentName, false);
        user.setName(name);
    }

    public void updateEmail(User user, Scanner scanner) {
        LOGGER.info("Enter new email (leave blank to keep current): ");
        String currentEmail = user.getEmail();
        String email = InputValidator.validateEmail(scanner, currentEmail, false);
        user.setEmail(email);
    }

    public void updateAge(User user, Scanner scanner) {
        LOGGER.info("Enter new age (leave blank to keep current): ");
        Integer currentAge = user.getAge();
        Integer age = InputValidator.validateAge(scanner, currentAge, false);
        user.setAge(age);
    }

    public void verifyUpdate(Long id) {
        Optional<User> updatedUserOptional = userService.findById(id);
        if (updatedUserOptional.isPresent()) {
            User updatedUser = updatedUserOptional.get();
            LOGGER.info("Verified updated data:");
            LOGGER.info("Name: {}, Email: {}, Age: {}",
                    updatedUser.getName(), updatedUser.getEmail(), updatedUser.getAge());
        } else {
            LOGGER.warn("Failed to verify updated user!");
        }
    }

}
