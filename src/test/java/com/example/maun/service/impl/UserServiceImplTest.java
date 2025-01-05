package com.example.maun.service.impl;

import com.example.maun.dto.NewUser;
import com.example.maun.dto.NewUserResponse;
import com.example.maun.dto.PhoneUser;
import com.example.maun.dto.UserResponse;
import com.example.maun.entity.Phone;
import com.example.maun.entity.User;
import com.example.maun.repository.PhoneRepository;
import com.example.maun.repository.UserRepository;
import com.example.maun.service.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    // Variables para datos de prueba que se usarán en múltiples tests
    private NewUser newUser;
    private User existingUser;
    private UUID userId;
    private String testToken;
    private List<PhoneUser> phoneUsers;

    @BeforeEach
    void setUp() {
        // Inicialización de datos de prueba comunes
        userId = UUID.randomUUID();
        testToken = "test-jwt-token";

        // Configuración de teléfonos de prueba
        phoneUsers = Arrays.asList(
                new PhoneUser(123456789L, 1, "57")
        );

        // Configuración del nuevo usuario
        newUser = new NewUser();
        newUser.setName("Test User");
        newUser.setEmail("test@test.com");
        newUser.setPassword("Test123");
        newUser.setPhones(phoneUsers);

        // Configuración del usuario existente
        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Test User");
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("Test123");
        existingUser.setCreated(LocalDateTime.now());
        existingUser.setModified(LocalDateTime.now());
        existingUser.setIsactive(true);
    }

    @Nested
    @DisplayName("Tests de registro de usuario")
    class RegisterUserTests {

        @Test
        @DisplayName("Registro exitoso de usuario con teléfonos")
        void whenRegisterNewUserWithPhones_thenSuccess() {
            // Configuración de los mocks
            when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
            when(userRepository.saveAndFlush(any(User.class))).thenReturn(existingUser);
            when(jwtService.generateToken(any(User.class), any())).thenReturn(testToken);

            // Ejecución del método a probar
            NewUserResponse response = userService.registerUser(newUser);

            // Verificaciones
            assertAll("Verificación del registro de usuario",
                    () -> assertNotNull(response, "La respuesta no debe ser nula"),
                    () -> assertEquals(userId, response.getId(), "El ID debe coincidir"),
                    () -> assertEquals(testToken, response.getToken(), "El token debe coincidir"),
                    () -> assertTrue(response.getIsActive(), "El usuario debe estar activo"),
                    () -> assertNotNull(response.getCreated(), "La fecha de creación no debe ser nula")
            );

            // Verificación de llamadas a los mocks
            verify(userRepository).findByEmail(newUser.getEmail());
            verify(userRepository).saveAndFlush(any(User.class));
            verify(phoneRepository).saveAllAndFlush(any());
            verify(jwtService).generateToken(any(), any());
            verify(jwtService).saveToken(any(), eq(testToken));
        }

        @Test
        @DisplayName("Registro de usuario sin teléfonos")
        void whenRegisterNewUserWithoutPhones_thenSuccess() {
            // Preparar datos de prueba sin teléfonos
            newUser.setPhones(null);

            when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
            when(userRepository.saveAndFlush(any(User.class))).thenReturn(existingUser);
            when(jwtService.generateToken(any(User.class), any())).thenReturn(testToken);

            NewUserResponse response = userService.registerUser(newUser);

            assertNotNull(response);
            assertEquals(userId, response.getId());

            verify(phoneRepository, never()).saveAllAndFlush(any());
        }

        @Test
        @DisplayName("Error al registrar usuario con email existente")
        void whenRegisterExistingEmail_thenThrowException() {
            when(userRepository.findByEmail(newUser.getEmail())).thenReturn(existingUser);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.registerUser(newUser),
                    "Debe lanzar una excepción cuando el email ya existe"
            );

            assertEquals("El correo ya está registrado", exception.getMessage());
            verify(userRepository).findByEmail(newUser.getEmail());
            verify(userRepository, never()).saveAndFlush(any());
            verify(phoneRepository, never()).saveAllAndFlush(any());
        }
    }

    @Nested
    @DisplayName("Tests de búsqueda de usuario")
    class SearchUserTests {

        @Test
        @DisplayName("Búsqueda exitosa de usuario por ID")
        void whenSearchValidUser_thenSuccess() {
            // Configurar comportamiento esperado
            when(jwtService.getToken(userId.toString())).thenReturn(testToken);
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // Ejecutar búsqueda
            UserResponse response = userService.buscarPorId(userId, testToken);

            // Verificaciones
            assertAll("Verificación de datos del usuario",
                    () -> assertNotNull(response),
                    () -> assertEquals(userId, response.getId()),
                    () -> assertEquals(existingUser.getName(), response.getName()),
                    () -> assertEquals(existingUser.getEmail(), response.getEmail()),
                    () -> assertEquals(testToken, response.getToken())
            );
        }

        @Test
        @DisplayName("Error al buscar usuario con token inválido")
        void whenSearchWithInvalidToken_thenThrowException() {
            String invalidToken = "invalid-token";
            when(jwtService.getToken(userId.toString())).thenReturn(testToken);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.buscarPorId(userId, invalidToken),
                    "Debe lanzar una excepción cuando el token es inválido"
            );

            assertEquals("Token inválido", exception.getMessage());
            verify(userRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Error al buscar usuario que no existe")
        void whenSearchNonExistentUser_thenThrowException() {
            when(jwtService.getToken(userId.toString())).thenReturn(testToken);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.buscarPorId(userId, testToken),
                    "Debe lanzar una excepción cuando el usuario no existe"
            );

            assertEquals("No existe un usuario con el identificador ingresado", exception.getMessage());
        }

        @Test
        @DisplayName("Error al buscar usuario con token vacío")
        void whenSearchWithEmptyToken_thenThrowException() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.buscarPorId(userId, ""),
                    "Debe lanzar una excepción cuando el token está vacío"
            );

            assertEquals("Token inválido", exception.getMessage());
            verify(userRepository, never()).findById(any());
        }
    }
}