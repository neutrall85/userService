package ru.aston.homework.intensive_modul2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static ru.aston.homework.intensive_modul2.Application.userDao;

public class UpdateUser implements UserChoiceStrategy{
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUser.class);

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter user ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        User user = userDao.findById(id).orElse(null);
        if (user != null) {
            LOGGER.info("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                user.setName(name);
            }

            LOGGER.info("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            LOGGER.info("Enter new age (leave blank to keep current): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            userDao.update(user);
            LOGGER.info("User updated successfully!");
        } else {
            LOGGER.info("User not found!");
        }
    }
}
