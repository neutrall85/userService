package ru.aston.homework.intensive.userservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "User response data")
public class UserResponseDto {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User's full name", example = "Leo Smirnoff")
    private String name;

    @Schema(description = "User's email address", example = "leo@mail.ru")
    private String email;

    @Schema(description = "User's age", example = "40")
    private Integer age;

    @Schema(description = "User creation timestamp", example = "2023-12-01T10:00:00")
    private LocalDateTime createdAt;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = createdAt != null ? createdAt.format(formatter) : "N/A";

        return  "┌─────────────────────────────────────────────────────────────┐\n"
              + "│                       👤 USER PROFILE                       │\n"
              + "├─────────────────────────────────────────────────────────────┤\n"
              + "│  ID:          " + String.format("%-40s", id) + "            │\n"
              + "│  Name:        " + String.format("%-40s", name) + "          │\n"
              + "│  Email:       " + String.format("%-40s", email) + "         │\n"
              + "│  Age:         " + String.format("%-40s", age + " years") + "│\n"
              + "│  Created:     " + String.format("%-40s", formattedDate) + " │\n"
              + "└─────────────────────────────────────────────────────────────┘";
    }
}
