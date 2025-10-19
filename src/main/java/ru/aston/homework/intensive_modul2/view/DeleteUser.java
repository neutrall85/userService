package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Scanner;

public final class DeleteUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUser.class);
    private final UserService userService;

    public DeleteUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter user ID to delete: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            if (userService.exists(id)) {
                userService.delete(id);
                LOGGER.info("User with ID {} deleted successfully!", id);
            } else {
                LOGGER.warn("User with ID {} not found!", id);
            }
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid user ID format: ", e);
        } catch (Exception e) {
            LOGGER.error("Error during user deletion: ", e);
        }
    }
}
