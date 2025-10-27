package ru.aston.homework.intensive_modul2.controller.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserResponseDtoTest {

    @Test
    void testUserResponseDtoDefaultConstructorShouldCreateEmptyObject() {
        UserResponseDto dto = new UserResponseDto();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getAge());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void testUserResponseDtoParameterizedConstructorShouldSetAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 30, 0);
        UserResponseDto dto = new UserResponseDto(1L, "Test User", "test@mail.ru", 30, createdAt);
        assertEquals(1L, dto.getId());
        assertEquals("Test User", dto.getName());
        assertEquals("test@mail.ru", dto.getEmail());
        assertEquals(30, dto.getAge());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testUserResponseDto_GettersAndSetters_ShouldWorkCorrectly() {
        UserResponseDto dto = new UserResponseDto();
        LocalDateTime createdAt = LocalDateTime.now();
        dto.setId(100L);
        dto.setName("Setter Test");
        dto.setEmail("setter@mail.ru");
        dto.setAge(40);
        dto.setCreatedAt(createdAt);
        assertEquals(100L, dto.getId());
        assertEquals("Setter Test", dto.getName());
        assertEquals("setter@mail.ru", dto.getEmail());
        assertEquals(40, dto.getAge());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testUserResponseDtoToStringWithDataShouldReturnFormattedString() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 25, 15, 30, 45);
        UserResponseDto dto = new UserResponseDto(1L, "John Doe", "john@mail.ru", 25, createdAt);
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@mail.ru"));
        assertTrue(result.contains("25 years"));
        assertTrue(result.contains("2023-12-25 15:30:45"));
        assertTrue(result.contains("USER PROFILE"));
    }

    @Test
    void testUserResponseDtoToStringWithNullCreatedAtShouldReturnFormattedString() {
        UserResponseDto dto = new UserResponseDto(1L, "John Doe", "john@mail.ru", 25, null);
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@mail.ru"));
        assertTrue(result.contains("25 years"));
        assertTrue(result.contains("N/A")); // Для null даты
    }

    @Test
    void testUserResponseDtoToStringWithBoundaryValuesShouldFormatCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        UserResponseDto dto = new UserResponseDto(
                999999999L,
                "Very Long Name That Should Be Truncated In Display",
                "very.long.email.address@mail.ru",
                120,
                createdAt
        );
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("999999999"));
        assertTrue(result.contains("Very Long Name That Should Be Truncated In Display"));
        assertTrue(result.contains("very.long.email.address@mail.ru"));
        assertTrue(result.contains("120 years"));
        assertTrue(result.contains("2023-01-01 00:00:00"));
    }

    @Test
    void testUserResponseDtoWithMinimumValuesShouldWorkCorrectly() {
        LocalDateTime minDate = LocalDateTime.MIN;
        UserResponseDto dto = new UserResponseDto(1L, "A", "a@b.c", 1, minDate);
        assertEquals(1L, dto.getId());
        assertEquals("A", dto.getName());
        assertEquals("a@b.c", dto.getEmail());
        assertEquals(1, dto.getAge());
        assertEquals(minDate, dto.getCreatedAt());
    }
}