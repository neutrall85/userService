package ru.aston.homework.intensive_modul2.controller.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoTest {

    private final Validator validator;

    public UserDtoTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testUserDtoWithValidDataShouldPassValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Valid User");
        dto.setEmail("valid@mail.ru");
        dto.setAge(25);
        dto.setCreatedAt("2023-01-01 10:00:00");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }

    @Test
    void testUserDtoWithNullNameShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName(null);
        dto.setEmail("test@mail.ru");
        dto.setAge(25);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Name cannot be null")));
    }

    @Test
    void testUserDtoWithNullEmailShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail(null);
        dto.setAge(25);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Email cannot be null")));
    }

    @Test
    void testUserDtoWithNullAgeShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@mail.ru");
        dto.setAge(null);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Age cannot be null")));
    }

    @Test
    void testUserDtoWithInvalidEmailFormatShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("invalid-email-format");
        dto.setAge(25);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Invalid email format")));
    }

    @Test
    void userDtoWithAgeLessThan1ShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@mail.ru");
        dto.setAge(0);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be greater than 0")));
    }

    @Test
    void testUserDtoGettersAndSettersShouldWorkCorrectly() {
        UserDto dto = new UserDto();
        dto.setId(99L);
        dto.setName("Getter Setter Test");
        dto.setEmail("getset@mail.ru");
        dto.setAge(50);
        dto.setCreatedAt("2023-12-31 23:59:59");
        assertEquals(99L, dto.getId());
        assertEquals("Getter Setter Test", dto.getName());
        assertEquals("getset@mail.ru", dto.getEmail());
        assertEquals(50, dto.getAge());
        assertEquals("2023-12-31 23:59:59", dto.getCreatedAt());
    }

    @Test
    void testUserDtoWithInvalidNameCharactersShouldFailValidation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Invalid@Name123");
        dto.setEmail("test@mail.ru");
        dto.setAge(25);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Invalid name format")));
    }
}
