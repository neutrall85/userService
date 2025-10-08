package ru.aston.homework.intensive_modul2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static ru.aston.homework.intensive_modul2.Application.userDao;

public class DeleteUser implements UserChoiceStrategy{
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUser.class);

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter user ID to delete: ");

        try {
            Long id = scanner.nextLong();
            scanner.nextLine(); // consume newline

            // Проверяем существование пользователя
            if (userDao.exists(id)) {
                userDao.delete(id);
                LOGGER.info("User with ID {} deleted successfully!", id);
            } else {
                LOGGER.warn("User with ID {} not found!", id);
            }

        } catch (Exception e) {
            LOGGER.error("Error during user deletion: ", e);
        }
    }
}
