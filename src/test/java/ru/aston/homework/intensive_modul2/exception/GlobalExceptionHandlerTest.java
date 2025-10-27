package ru.aston.homework.intensive_modul2.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aston.homework.intensive_modul2.controller.UserController;
import ru.aston.homework.intensive_modul2.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserController userController = new UserController(userService);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testHandleUserNotFoundException() throws Exception {
        when(userService.getUserById(999L))
                .thenThrow(new UserNotFoundException(999L));
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testHandleUserNotFoundExceptionForUpdate() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new UserNotFoundException(999L));
        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":25}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"));
    }

    @Test
    void testHandleUserNotFoundExceptionForDelete() throws Exception {
        doThrow(new UserNotFoundException(999L))
                .when(userService).deleteUser(999L);
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"));
    }

    @Test
    void testHandleEmailAlreadyExistsException() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new EmailAlreadyExistsException("existing@example.com"));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"existing@example.com\",\"age\":25}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email Already Exists"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testHandleEmailAlreadyExistsExceptionForUpdate() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new EmailAlreadyExistsException("existing@example.com"));
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"existing@example.com\",\"age\":25}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email Already Exists"));
    }

    @Test
    void testHandleValidationExceptions_NullName() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"valid@example.com\",\"age\":25}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleValidationExceptions_InvalidEmail() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"invalid-email\",\"age\":25}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleValidationExceptions_NullEmail() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"age\":25}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleValidationExceptions_AgeTooLow() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleValidationExceptions_AgeTooHigh() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":151}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleValidationExceptions_NullAge() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_InvalidJson() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid-json-content"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_EmptyBody() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_MalformedJson() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test\", \"email\": \"test@example.com\", \"age\": }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/api/users/not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Argument Type"));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException_ForOtherEndpoints() throws Exception {
        mockMvc.perform(put("/api/users/not-a-number")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":25}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Argument Type"));
    }

    @Test
    void testHandleGenericException() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenThrow(new RuntimeException("Database connection failed"));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void testHandleGenericExceptionInCreate() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":25}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void testHandleGenericExceptionInUpdate() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new RuntimeException("Update failed"));
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":25}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void testHandleGenericExceptionInDelete() throws Exception {
        doThrow(new RuntimeException("Delete failed"))
                .when(userService).deleteUser(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void testHandleVeryLongInput() throws Exception {
        String veryLongName = "A".repeat(1000);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + veryLongName + "\",\"email\":\"test@example.com\",\"age\":25}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_AgeBranch() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":\"invalid-age\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"))
                .andExpect(jsonPath("$.message").value("Age must be a valid number"))
                .andExpect(jsonPath("$.detail").value("Check that all fields have correct data types"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_IntegerBranch() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\":true}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"))
                .andExpect(jsonPath("$.message").value("Numeric field contains invalid value"))
                .andExpect(jsonPath("$.detail").value("Check that all fields have correct data types"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_JsonParseBranch() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"age\": }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Body"))
                .andExpect(jsonPath("$.message").value("Invalid JSON format in request body"))
                .andExpect(jsonPath("$.detail").value("Check that all fields have correct data types"));
    }

    @Test
    void testHandleHttpMessageNotReadableException_NullMessageBranch() throws Exception {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException( null);
        java.lang.reflect.Method method = GlobalExceptionHandler.class.getDeclaredMethod("getString", HttpMessageNotReadableException.class);
        method.setAccessible(true);
        String result = (String) method.invoke(handler, ex);

        assertEquals("Invalid JSON format", result);
    }
}