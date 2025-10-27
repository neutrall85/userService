package ru.aston.homework.intensive_modul2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UserResponseDto;
import ru.aston.homework.intensive_modul2.exception.GlobalExceptionHandler;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "User 1", "user1@example.com", 25, LocalDateTime.now());
        UserResponseDto user2 = new UserResponseDto(2L, "User 2", "user2@example.com", 30, LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetUserById() throws Exception {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
        user.setCreatedAt(LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User found successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test User"));

        verify(userService).getUserById(1L);
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("New User");
        createDto.setEmail("new@example.com");
        createDto.setAge(25);

        UserResponseDto createdUser = new UserResponseDto();
        createdUser.setId(1L);
        createdUser.setName("New User");
        createdUser.setEmail("new@example.com");
        createdUser.setAge(25);
        createdUser.setCreatedAt(LocalDateTime.now());

        when(userService.createUser(any(CreateUserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("New User"));

        verify(userService).createUser(any(CreateUserDto.class));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.path").exists());

        verify(userService, never()).createUser(any(CreateUserDto.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name");
        updateDto.setAge(26);

        UserResponseDto updatedUser = new UserResponseDto();
        updatedUser.setId(1L);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("test@example.com");
        updatedUser.setAge(26);

        when(userService.updateUser(eq(1L), any(UpdateUserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.age").value(26));

        verify(userService).updateUser(eq(1L), any(UpdateUserDto.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.deletedId").value(1));

        verify(userService).deleteUser(1L);
    }
}