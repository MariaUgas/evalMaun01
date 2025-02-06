package com.example.maun.controller;


import com.example.maun.dto.NewUser;
import com.example.maun.dto.NewUserResponse;
import com.example.maun.dto.UserResponse;
import com.example.maun.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private NewUser newUser;
    private NewUserResponse newUserResponse;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Preparar datos de prueba
        newUser = new NewUser();
        newUser.setEmail("test@test.com");
        newUser.setPassword("password123");
        newUser.setName("Test User");

        newUserResponse = new NewUserResponse();
        newUserResponse.setId(userId);


        userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setEmail("test@test.com");
    }

    @Test
    void registrarUsuario_Success() {
        // Given
        when(userService.registerUser(any(NewUser.class)))
                .thenReturn(newUserResponse);

        // When
        ResponseEntity<NewUserResponse> response = userController
                .registrarUsuario(newUser);

        // Then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(newUserResponse.getId(), response.getBody().getId())
        );
        verify(userService).registerUser(any(NewUser.class));
    }

    @Test
    void registrarUsuario_ThrowsException() {
        // Given
        when(userService.registerUser(any(NewUser.class)))
                .thenThrow(new RuntimeException("Error"));

        // Then
        assertThrows(RuntimeException.class, () -> {
            userController.registrarUsuario(newUser);
        });
    }

    @Test
    void buscarUsuarios_Success() {
        // Given
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        when(userService.buscarPorId(eq(userId), anyString()))
                .thenReturn(userResponse);

        // When
        ResponseEntity<UserResponse> response = userController
                .buscarUsuarios(userId, token);

        // Then
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(userId, response.getBody().getId())
        );
        verify(userService).buscarPorId(eq(userId), anyString());
    }

    @Test
    void buscarUsuarios_UnauthorizedNoToken() {
        // When/Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userController.buscarUsuarios(userId, null)
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void buscarUsuarios_UnauthorizedInvalidToken() {
        // Given
        String invalidToken = "InvalidToken";
        // When/Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userController.buscarUsuarios(userId, invalidToken)
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void buscarUsuarios_ServiceThrowsException() {
        // Given
        String token = "Bearer validToken";
        when(userService.buscarPorId(any(UUID.class), anyString()))
                .thenThrow(new RuntimeException("Error"));

        // Then
        assertThrows(RuntimeException.class, () -> {
            userController.buscarUsuarios(userId, token);
        });
    }
}