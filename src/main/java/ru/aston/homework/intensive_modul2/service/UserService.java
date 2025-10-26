package ru.aston.homework.intensive_modul2.service;

import org.springframework.transaction.annotation.Transactional;
import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(CreateUserDto createUserDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UpdateUserDto updateUserDto);
    void deleteUser(Long id);
    boolean existsById(Long id);

    @Transactional(readOnly = true)
    boolean existsByEmail(String email);
}

