package ru.aston.homework.intensive_modul2.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class InputValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputValidator.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[а-яА-Яa-zA-Z\\s-]+$");
    private InputValidator() { }

    public static String validateEmailForUpdate(Scanner scanner, String currentEmail) {
        return validateEmail(scanner, currentEmail, false);
    }

    public static Integer validateAgeForUpdate(Scanner scanner, Integer currentAge) {
        return validateAge(scanner, currentAge, false);
    }

    public static String validateNameForUpdate(Scanner scanner, String currentName) {
        return validateName(scanner, currentName, false);
    }

    public static String validateEmailForCreate(Scanner scanner) {
        return validateEmail(scanner, null, true);
    }

    public static Integer validateAgeForCreate(Scanner scanner) {
        return validateAge(scanner, null, true);
    }

    public static String validateNameForCreate(Scanner scanner) {
        return validateName(scanner, null, true);
    }

    static String validateEmail(Scanner scanner, String currentEmail, boolean isRequired) {
        String email;
        int attemptCount = 0;
        final int maxAttempts = 3;

        while (attemptCount < maxAttempts) {
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                if (isRequired) {
                    LOGGER.warn("Email cannot be empty. Please try again (example: user@domain.com).");
                    attemptCount++;
                    continue;
                } else {
                    return currentEmail;
                }
            }
            if (isValidEmail(email)) {
                return email;
            } else {
                LOGGER.warn("Please enter a valid email address (example: user@domain.com).");
                attemptCount++;
            }
        }
        throw new IllegalArgumentException("Maximum attempts exceeded. Unable to validate email.");
    }

    static Integer validateAge(Scanner scanner, Integer currentAge, boolean isRequired) {
        int attemptCount = 0;
        final int maxAttempts = 3;

        while (attemptCount < maxAttempts) {
            String ageStr = scanner.nextLine().trim();
            if (ageStr.isEmpty()) {
                if (isRequired) {
                    LOGGER.warn("Age cannot be empty. Please try again.");
                    attemptCount++;
                    continue;
                } else {
                    return currentAge;
                }
            }
            try {
                int age = Integer.parseInt(ageStr);
                if (age > 0 && age <= 120) {
                    return age;
                } else {
                    LOGGER.warn("Age cannot be negative or exceed 120. Please try again.");
                    attemptCount++;
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid input. Please enter a numeric value for age.");
                attemptCount++;
            }
        }
        throw new IllegalArgumentException("Maximum attempts exceeded. Unable to validate age.");
    }

    static String validateName(Scanner scanner, String currentName, boolean isRequired) {
        String name;
        int attemptCount = 0;
        final int maxAttempts = 3;

        while (attemptCount < maxAttempts) {
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                if (isRequired) {
                    LOGGER.warn("Name cannot be empty. Please try again.");
                    attemptCount++;
                    continue;
                } else {
                    return currentName;
                }
            }
            if (isValidName(name)) {
                return name;
            } else {
                LOGGER.warn("Please enter a valid name (letters, spaces, and hyphens are allowed).");
                attemptCount++;
            }
        }
        throw new IllegalArgumentException("Maximum attempts exceeded. Unable to validate name.");
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }
}
