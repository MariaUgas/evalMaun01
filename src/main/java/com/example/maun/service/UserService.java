package com.example.maun.service;

import com.example.maun.dto.NewUser;
import com.example.maun.dto.NewUserResponse;
import com.example.maun.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public interface UserService {
    NewUserResponse registerUser(NewUser newUser);
    UserResponse buscarPorId(UUID id, String token);
}