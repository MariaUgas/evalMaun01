package com.example.maun.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatedRedisService {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> expiryTimes = new ConcurrentHashMap<>();

    public void saveToken(String key, String token, long expirationTime) {
        long expiryTimeInMillis = System.currentTimeMillis() + expirationTime;
        store.put(key, token); expiryTimes.put(key, expiryTimeInMillis);
    }

    public String getToken(String key) {
        Long expiryTime = expiryTimes.get(key);
        if (expiryTime != null && System.currentTimeMillis() > expiryTime) {
            store.remove(key);
            expiryTimes.remove(key);
            return null;
        } return store.get(key);
    }
    // Limpieza periódica de elementos expirados (opcional)
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        expiryTimes.forEach((key, expiryTime) -> {
            if (now > expiryTime) {
                store.remove(key);
                expiryTimes.remove(key);
            }
        });
    }
}
