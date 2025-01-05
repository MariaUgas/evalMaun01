package com.example.maun.service.impl;

import com.example.maun.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private SimulatedRedisService redis;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private User user;
    private Map<String, Object> extraClaims;

    @BeforeEach
    void setUp() {
        // Configurar propiedades necesarias usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "EXPIRATION_MINUTES", 60L);
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");

        // Preparar datos de prueba
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setPassword("Test123");

        extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
    }

    @Test
    void generateToken_Success() {
        // Generar token
        String token = jwtService.generateToken(user, extraClaims);

        // Verificaciones
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void saveAndGetToken_Success() {
        String key = "test-key";
        String token = "test-token";

        // Guardar token
        jwtService.saveToken(key, token);

        // Configurar comportamiento del mock para getToken
        when(redis.getToken(key)).thenReturn(token);

        // Obtener token
        String retrievedToken = jwtService.getToken(key);

        // Verificaciones
        assertEquals(token, retrievedToken);
        verify(redis).saveToken(key, token, 60L * 60 * 1000);
        verify(redis).getToken(key);
    }
}