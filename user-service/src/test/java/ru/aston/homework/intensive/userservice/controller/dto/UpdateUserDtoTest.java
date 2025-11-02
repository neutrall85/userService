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
class UpdateUserDtoTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void testUpdateUserDtoWithValidDataShouldPassValidation() {
        UpdateUserDto dto = new UpdateUserDto("John", "john@mail.ru", 25);
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }

    @Test
    void testUpdateUserDtoWithEmptyFieldsShouldPassValidation() {
        UpdateUserDto dto = new UpdateUserDto();
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Update DTO should allow empty fields");
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
        dto.setEmail("updated@mail.ru");
        dto.setAge(35);
        assertEquals("Updated Name", dto.getName());
        assertEquals("updated@mail.ru", dto.getEmail());
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

    @Test
    void testUpdateUserDtoWithValidPartialUpdate() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Valid Name");
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateUserDtoWithOnlyEmail() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setEmail("valid@mail.ru");
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateUserDtoWithOnlyAge() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setAge(30);
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
