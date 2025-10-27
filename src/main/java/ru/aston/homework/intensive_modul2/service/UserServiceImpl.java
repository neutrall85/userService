package ru.aston.homework.intensive_modul2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UserResponseDto;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.exception.EmailAlreadyExistsException;
import ru.aston.homework.intensive_modul2.exception.UserNotFoundException;
import ru.aston.homework.intensive_modul2.repository.UserRepository;
import ru.aston.homework.intensive_modul2.util.ValidationUtil;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        LOGGER.info("Getting user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("User not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });

        LOGGER.info("Successfully retrieved user: {} (ID: {})", user.getName(), user.getId());
        return toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        LOGGER.info("Getting all users");

        List<User> users = userRepository.findAll();
        LOGGER.info("Retrieved {} users from database", users.size());

        return users.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserResponseDto createUser(CreateUserDto createUserDto) {
        LOGGER.info("Creating new user: {}", createUserDto.getEmail());

        ValidationUtil.validateCreateUserDto(createUserDto);

        if (createUserDto.getAge() == null) {
            LOGGER.error("Age cannot be null for user creation");
            throw new IllegalArgumentException("Age cannot be null");
        }

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            LOGGER.warn("Email already exists: {}", createUserDto.getEmail());
            throw new EmailAlreadyExistsException(createUserDto.getEmail());
        }

        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setAge(createUserDto.getAge());

        try {
            User savedUser = userRepository.save(user);
            LOGGER.info("Successfully created user: {} (ID: {})", savedUser.getName(), savedUser.getId());
            return toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Data integrity violation while creating user: {}", e.getMessage());
            throw new EmailAlreadyExistsException(createUserDto.getEmail());
        } catch (Exception e) {
            LOGGER.error("Unexpected error while creating user: {}", e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public UserResponseDto updateUser(Long id, UpdateUserDto updateUserDto) {
        LOGGER.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("User not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });

        ValidationUtil.validateUpdateUserDto(updateUserDto);

        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
            LOGGER.debug("Updated name for user ID: {}", id);
        }

        if (updateUserDto.getEmail() != null) {
            if (userRepository.existsByEmailAndIdNot(updateUserDto.getEmail(), id)) {
                LOGGER.warn("Email already exists for update: {}", updateUserDto.getEmail());
                throw new EmailAlreadyExistsException(updateUserDto.getEmail());
            }
            user.setEmail(updateUserDto.getEmail());
            LOGGER.debug("Updated email for user ID: {}", id);
        }

        if (updateUserDto.getAge() != null) {
            user.setAge(updateUserDto.getAge());
            LOGGER.debug("Updated age for user ID: {}", id);
        }

        try {
            User updatedUser = userRepository.save(user);
            LOGGER.info("Successfully updated user: {} (ID: {})", updatedUser.getName(), updatedUser.getId());
            return toDto(updatedUser);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Data integrity violation while updating user: {}", e.getMessage());
            throw new EmailAlreadyExistsException(updateUserDto.getEmail());
        } catch (Exception e) {
            LOGGER.error("Unexpected error while updating user: {}", e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            LOGGER.warn("Attempt to delete non-existent user with ID: {}", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        LOGGER.info("Successfully deleted user with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}

