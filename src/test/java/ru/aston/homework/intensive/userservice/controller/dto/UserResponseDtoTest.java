package ru.aston.homework.intensive.userservice.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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
        UserResponseDto dto = new UserResponseDto(1L, "test", "test@mail.ru", 30, createdAt);
        assertEquals(1L, dto.getId());
        assertEquals("test", dto.getName());
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
        UserResponseDto dto = new UserResponseDto(1L, "John", "john@mail.ru", 25, createdAt);
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("John"));
        assertTrue(result.contains("john@mail.ru"));
        assertTrue(result.contains("25 years"));
        assertTrue(result.contains("2023-12-25 15:30:45"));
        assertTrue(result.contains("USER PROFILE"));
    }

    @Test
    void testUserResponseDtoToStringWithNullCreatedAtShouldReturnFormattedString() {
        UserResponseDto dto = new UserResponseDto(1L, "John", "john@mail.ru", 25, null);
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("John"));
        assertTrue(result.contains("john@mail.ru"));
        assertTrue(result.contains("25 years"));
        assertTrue(result.contains("N/A"));
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

    @Test
    void testUserResponseDtoEqualsAndHashCode() {
        LocalDateTime createdAt = LocalDateTime.now();
        UserResponseDto dto1 = new UserResponseDto(1L, "User", "user@mail.ru", 25, createdAt);
        UserResponseDto dto2 = new UserResponseDto(1L, "User", "user@mail.ru", 25, createdAt);
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testUserResponseDtoWithNullValues() {
        UserResponseDto dto = new UserResponseDto(null, null, null, null, null);
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getAge());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void testUserResponseDtoWithMaximumAge() {
        LocalDateTime createdAt = LocalDateTime.now();
        UserResponseDto dto = new UserResponseDto(1L, "Max Age User", "max@mail.ru", 120, createdAt);
        assertEquals(120, dto.getAge());
    }

    @Test
    void testUserResponseDtoWithLongEmail() {
        LocalDateTime createdAt = LocalDateTime.now();
        String longEmail = "very.long.email.address.with.many.parts@example-domain.mail.ru";
        UserResponseDto dto = new UserResponseDto(1L, "test", longEmail, 25, createdAt);
        assertEquals(longEmail, dto.getEmail());
    }
}
