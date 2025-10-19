package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Optional;
import java.util.Scanner;

public final class FindUserByID implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindUserByID.class);
    private final UserService userService;

    public FindUserByID(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter user ID: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                LOGGER.info("""
                        User found:
                        ID: {}  Name: {}  E-mail: {}  Age: {}  Created at: {}
                        """, user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
            } else {
                LOGGER.warn("User with ID {} not found!", id);
            }
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid user ID format: ", e);
        } catch (Exception e) {
            LOGGER.error("Error while finding user: ", e);
        }
    }
}
