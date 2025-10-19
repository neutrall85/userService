package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public final class ListAllUsers implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAllUsers.class);
    private final UserService userService;

    public ListAllUsers(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Starting to list all users...");
        try {
            List<User> users = userService.findAll();
            if (users == null || users.isEmpty()) {
                LOGGER.warn("No users found!");
                return;
            }
            LOGGER.info("All users:");
            String userFormat = """
                    User found:
                    ID: {}  Name: {}  E-mail: {}  Age: {}  Created at: {}
                    """;
            users.stream()
                    .sorted(Comparator.comparing(User::getId))
                    .forEach(user -> LOGGER.info(userFormat,
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getAge(),
                            user.getCreatedAt()));
        } catch (Exception e) {
            LOGGER.error("Error while listing users: ", e);
        }
        LOGGER.info("Finished listing all users.");
    }
}
