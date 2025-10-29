package ru.aston.homework.intensive.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.homework.intensive.userservice.controller.dto.CreateUserDto;
import ru.aston.homework.intensive.userservice.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive.userservice.entity.User;
import ru.aston.homework.intensive.userservice.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Test");
        testUser.setEmail("test@mail.ru");
        testUser.setAge(25);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser = userRepository.save(testUser);
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user2 = new User("User 2", "user2@mail.ru", 30);
        user2.setCreatedAt(LocalDateTime.now());
        userRepository.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    void testGetAllUsers_EmptyList() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"));
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("New User");
        createDto.setEmail("new@mail.ru");
        createDto.setAge(25);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("new@mail.ru"))
                .andExpect(jsonPath("$.age").value(25));
        assertTrue(userRepository.findByEmail("new@mail.ru").isPresent());
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("");
        createDto.setEmail("invalid-email");
        createDto.setAge(150);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertFalse(userRepository.findByEmail("invalid-email").isPresent());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("Another User");
        createDto.setEmail("test@mail.ru");
        createDto.setAge(30);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email Already Exists"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name");
        updateDto.setAge(26);

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.age").value(26));
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals(26, updatedUser.getAge());
        assertEquals("test@mail.ru", updatedUser.getEmail());
    }

    @Test
    void testUpdateUser_WithEmail() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@mail.ru");
        updateDto.setAge(26);

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@mail.ru"))
                .andExpect(jsonPath("$.age").value(26));
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("updated@mail.ru", updatedUser.getEmail());
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name");

        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"));
    }
}
