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

class UpdateUserDtoTest {

    private final Validator validator;

    public UpdateUserDtoTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testUpdateUserDtoWithValidDataShouldPassValidation() {
        UpdateUserDto dto = new UpdateUserDto("John Doe", "john@example.com", 25);
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }

    @Test
    void testUpdateUserDtoWithEmptyFieldsShouldPassValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Update DTO должен позволять пустые поля");
    }

    @Test
    void testUpdateUserDtoWithPartialDataShouldPassValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Updated Name");
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateUserDtoWithInvalidEmailShouldFailValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setEmail("invalid-email");
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testUpdateUserDtoWithInvalidAgeShouldFailValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setAge(150);
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("cannot exceed 120")));
    }

    @Test
    void testUpdateUserDtoGettersAndSettersShouldWorkCorrectly() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Updated Name");
        dto.setEmail("updated@example.com");
        dto.setAge(35);
        assertEquals("Updated Name", dto.getName());
        assertEquals("updated@example.com", dto.getEmail());
        assertEquals(35, dto.getAge());
    }

    @Test
    void testUpdateUserDtoConstructorShouldSetFieldsCorrectly() {
        UpdateUserDto dto = new UpdateUserDto("Constructor Test", "constructor@test.com", 45);
        assertEquals("Constructor Test", dto.getName());
        assertEquals("constructor@test.com", dto.getEmail());
        assertEquals(45, dto.getAge());
    }

    @Test
    void testUpdateUserDtoWithInvalidNameCharactersShouldFailValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Invalid123");
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("only contain letters")));
    }
}