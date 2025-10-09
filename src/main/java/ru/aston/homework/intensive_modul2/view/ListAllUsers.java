package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.List;
import java.util.Scanner;

public class ListAllUsers implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAllUsers.class);
    private final UserService userService;

    public ListAllUsers(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        List<User> users = userService.findAll();
        if (!users.isEmpty()) {
            LOGGER.info("All users:");
            for (User user : users) {
                LOGGER.info("""
                \s
                User found: \033[32m
                ID: {}
                Name: {}
                E-mail: {}
                Age: {}
                Created at: {}
                \033[0m""", user.getId(), user.getName(), user.getEmail(),
                            user.getAge(), user.getCreatedAt());
            }
        } else {
            LOGGER.info("\033[31mNo users found!\033[0m");
        }
    }
}

