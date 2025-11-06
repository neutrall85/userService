package ru.aston.homework.intensive.userservice.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Data required to create a new user")
public class CreateUserDto {

    @Schema(description = "User's full name", example = "Leo Smirnoff", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[а-яА-Яa-zA-Z\\s-]+$", message = "Name can only contain letters, spaces and hyphens")
    private String name;

    @Schema(
            description = "User's email address",
            example = "leo@mail.ru",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User's age", example = "40", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Age cannot be null")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age cannot exceed 120")
    private Integer age;

    public CreateUserDto() { }

    public CreateUserDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
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
}
