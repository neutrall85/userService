package ru.aston.homework.intensive_modul2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static ru.aston.homework.intensive_modul2.Application.USER_DAO;

public class UpdateUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUser.class);

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("\033[33mEnter user ID to update: \033[0m");
        Long id = scanner.nextLong();
        scanner.nextLine();

        User user = USER_DAO.findById(id).orElse(null);
        if (user != null) {
            LOGGER.info("\033[33mEnter new name (leave blank to keep current): \033[0m");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                user.setName(name);
            }

            LOGGER.info("\033[33mEnter new email (leave blank to keep current): \033[0m");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            LOGGER.info("\033[33mEnter new age (leave blank to keep current): \033[0m");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            USER_DAO.update(user);
            LOGGER.info("\033[32mUser updated successfully!\033[0m");
        } else {
            LOGGER.info("\033[31mUser not found!\033[0m");
        }
    }
}
