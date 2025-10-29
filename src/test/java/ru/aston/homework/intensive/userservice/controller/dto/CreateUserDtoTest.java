package ru.aston.homework.intensive.userservice.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreateUserDtoTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void createUserDtoWithValidDataShouldPassValidation() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }

    @Test
    void testCreateUserDtoWithInvalidEmailShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John", "invalid-email", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testCreateUserDtoWithAgeLessThan1ShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 0);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be at least 1")));
    }

    @Test
    void testCreateUserDtoWithAgeGreaterThan120ShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 121);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("cannot exceed 120")));
    }

    @Test
    void testCreateUserDtoWithInvalidNameCharactersShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("John123", "john@mail.ru", 25);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("only contain letters")));
    }

    @Test
    void testCreateUserDtoGettersAndSettersShouldWorkCorrectly() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("Jane");
        dto.setEmail("jane@mail.ru");
        dto.setAge(30);
        assertEquals("Jane", dto.getName());
        assertEquals("jane@mail.ru", dto.getEmail());
        assertEquals(30, dto.getAge());
    }

    @Test
    void testCreateUserDtoConstructorShouldSetFieldsCorrectly() {
        CreateUserDto dto = new CreateUserDto("Test", "test@mail.ru", 40);
        assertEquals("Test", dto.getName());
        assertEquals("test@mail.ru", dto.getEmail());
        assertEquals(40, dto.getAge());
    }

    @Test
    void testCreateUserDtoWithNullAgeShouldFailValidation() {
        CreateUserDto dto = new CreateUserDto("Test", "test@mail.ru", null);
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("cannot be null")));
    }
}
