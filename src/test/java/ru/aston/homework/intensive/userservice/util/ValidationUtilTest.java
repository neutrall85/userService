package ru.aston.homework.intensive.userservice.util;

import org.junit.jupiter.api.Test;
import ru.aston.homework.intensive.userservice.controller.dto.CreateUserDto;
import ru.aston.homework.intensive.userservice.controller.dto.UpdateUserDto;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void testValidateCreateUserDtoSuccess() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 25);
        assertDoesNotThrow(() -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_NullDto() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(null));
    }

    @Test
    void testValidateCreateUserDto_EmptyName() {
        CreateUserDto dto = new CreateUserDto("", "john@mail.ru", 25);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_InvalidEmail() {
        CreateUserDto dto = new CreateUserDto("John", "invalid-email", 25);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_EmptyEmail() {
        CreateUserDto dto = new CreateUserDto("John", "", 25);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_AgeTooOld() {
        CreateUserDto dto = new CreateUserDto("Joh", "john@mail.ru", 121);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_MinAge() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 18);
        assertDoesNotThrow(() -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_MaxAge() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", 120);
        assertDoesNotThrow(() -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_ZeroAge() {
        CreateUserDto dto = new CreateUserDto("John Doe", "john@mail.ru", 0);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateCreateUserDto_NegativeAge() {
        CreateUserDto dto = new CreateUserDto("John", "john@mail.ru", -5);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateCreateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_Success() {
        UpdateUserDto dto = new UpdateUserDto("Leo", "leo@mail.ru", 30);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_PartialUpdate() {
        UpdateUserDto dto = new UpdateUserDto("Leo", null, null);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_NullDto() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUpdateUserDto(null));
    }

    @Test
    void testValidateUpdateUserDto_EmptyName() {
        UpdateUserDto dto = new UpdateUserDto("", "email@mail.ru", 30);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_InvalidEmail() {
        UpdateUserDto dto = new UpdateUserDto("Name", "invalid-email", 30);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_EmptyEmail() {
        UpdateUserDto dto = new UpdateUserDto("Name", "", 30);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_InvalidAgeTooOld() {
        UpdateUserDto dto = new UpdateUserDto("Name", "email@mail.ru", 121);
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_ValidMinAge() {
        UpdateUserDto dto = new UpdateUserDto("Name", "email@mail.ru", 18);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_ValidMaxAge() {
        UpdateUserDto dto = new UpdateUserDto("Name", "email@mail.ru", 120);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_OnlyNameValid() {
        UpdateUserDto dto = new UpdateUserDto("Valid Name", null, null);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_OnlyEmailValid() {
        UpdateUserDto dto = new UpdateUserDto(null, "valid@mail.ru", null);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }

    @Test
    void testValidateUpdateUserDto_OnlyAgeValid() {
        UpdateUserDto dto = new UpdateUserDto(null, null, 25);
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateUserDto(dto));
    }
}
