package ru.aston.homework.intensive_modul2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import java.util.regex.Pattern;
import static ru.aston.homework.intensive_modul2.Application.userDao;

public class CreateUser implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUser.class);
    // Регулярное выражение для проверки email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    @Override
    public void invoke(Scanner scanner) {
        LOGGER.info("Enter name: ");
        String name = scanner.nextLine();

        String email = null;
        boolean isValidEmail = false;
        while (!isValidEmail) {
            LOGGER.info("Enter email: ");
            email = scanner.nextLine();

            if (isValidEmail(email)) {
                isValidEmail = true;
            } else {
                LOGGER.warn("Invalid email format. Please try again.");
            }
        }

        LOGGER.info("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        Long id = userDao.create(user);
        LOGGER.info("Created user with ID: {}", id);
    }


    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
