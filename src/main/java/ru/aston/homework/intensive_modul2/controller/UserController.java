package ru.aston.homework.intensive_modul2.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UserResponseDto;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private static final String STATUS = "status";
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, "Retrieved " + users.size() + " user(s)");
        response.put("data", users);
        response.put("count", users.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") Long id) {
        UserResponseDto user = userService.getUserById(id);

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, "User found successfully");
        response.put("data", user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserResponseDto createdUser = userService.createUser(createUserDto);

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, "User created successfully");
        response.put("data", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserDto updateUserDto) {
        UserResponseDto updatedUser = userService.updateUser(id, updateUserDto);

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, "User updated successfully");
        response.put("data", updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, "User deleted successfully");
        response.put("deletedId", id);

        return ResponseEntity.ok(response);
    }
}

