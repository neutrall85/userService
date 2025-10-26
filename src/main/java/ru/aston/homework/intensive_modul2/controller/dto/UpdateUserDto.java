package ru.aston.homework.intensive_modul2.controller.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[а-яА-Яa-zA-Z\\s-]*$", message = "Name can only contain letters, spaces and hyphens")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age cannot exceed 120")
    private Integer age;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Геттеры и сеттеры
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
}
