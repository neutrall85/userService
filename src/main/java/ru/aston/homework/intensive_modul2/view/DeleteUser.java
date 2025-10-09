package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.Scanner;

public class DeleteUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUser.class);
    private final UserService userService;

    public DeleteUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("\033[33mEnter user ID to delete: \033[0m");

        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            if (userService.exists(id)) {
                userService.delete(id);
                LOGGER.info("\033[31mUser with ID {} deleted successfully!\033[0m", id);
            } else {
                LOGGER.warn("\033[31mUser with ID {} not found!\033[0m", id);
            }

        } catch (Exception e) {
            LOGGER.error("\033[31mError during user deletion: \033[0m", e);
        }
    }
}
