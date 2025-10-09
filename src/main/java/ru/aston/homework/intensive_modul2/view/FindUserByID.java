package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.Optional;
import java.util.Scanner;

public class FindUserByID implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindUserByID.class);
    private final UserService userService;

    public FindUserByID(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter user ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            LOGGER.info("""
            \s
            User found: \033[32m
            ID: {}
            Name: {}
            E-mail: {}
            Age: {}
            Created at: {}
            \033[0m""", user.get().getId(), user.get().getName(), user.get().getEmail(),
                               user.get().getAge(), user.get().getCreatedAt());
        } else {
            LOGGER.info("\033[31mUser not found!\033[0m");
        }
    }
}
