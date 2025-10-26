package ru.aston.homework.intensive_modul2.util;

import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void validateCreateUserDto(CreateUserDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        // Дополнительная валидация поверх аннотаций
        if (dto.getName() != null && !dto.getName().matches("^[а-яА-Яa-zA-Z\\s-]+$")) {
            throw new IllegalArgumentException("Name can only contain letters, spaces and hyphens");
        }

        if (dto.getEmail() != null && isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (dto.getAge() != null && (dto.getAge() < 1 || dto.getAge() > 120)) {
            throw new IllegalArgumentException("Age must be between 1 and 120");
        }
    }

    public static void validateUpdateUserDto(UpdateUserDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        // Валидация только если поля не null
        if (dto.getName() != null) {
            if (dto.getName().length() < 2 || dto.getName().length() > 50) {
                throw new IllegalArgumentException("Name must be between 2 and 50 characters");
            }
            if (!dto.getName().matches("^[а-яА-Яa-zA-Z\\s-]*$")) {
                throw new IllegalArgumentException("Name can only contain letters, spaces and hyphens");
            }
        }

        if (dto.getEmail() != null && isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (dto.getAge() != null && (dto.getAge() < 1 || dto.getAge() > 120)) {
            throw new IllegalArgumentException("Age must be between 1 and 120");
        }
    }

    private static boolean isValidEmail(String email) {
        return email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
