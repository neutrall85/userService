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

class CreateUserDtoTest {

    private final Validator validator;

    public CreateUserDtoTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void createUserDtoWithValidDataShouldPassValidation() {
        CreateUserDto dto = new CreateUserDto("John Doe", "john@example.com", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }

    @Test
    void testCreateUserDtoWithInvalidEmailShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John Doe", "invalid-email", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testCreateUserDtoWithAgeLessThan1ShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John Doe", "john@example.com", 0);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be at least 1")));
    }

    @Test
    void testCreateUserDtoWithAgeGreaterThan120ShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John Doe", "john@example.com", 121);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("cannot exceed 120")));
    }

    @Test
    void testCreateUserDtoWithInvalidNameCharactersShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John123", "john@example.com", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("only contain letters")));
    }

    @Test
    void testCreateUserDtoGettersAndSettersShouldWorkCorrectly() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setAge(30);
        assertEquals("Jane Doe", dto.getName());
        assertEquals("jane@example.com", dto.getEmail());
        assertEquals(30, dto.getAge());
    }

    @Test
    void testCreateUserDtoConstructorShouldSetFieldsCorrectly() {
        CreateUserDto dto = new CreateUserDto("Test User", "test@example.com", 40);
        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(40, dto.getAge());
    }
}