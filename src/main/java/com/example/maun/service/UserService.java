package com.example.maun.service;

import com.example.maun.dto.NewUser;
import com.example.maun.dto.NewUserResponse;
import com.example.maun.dto.PhoneUser;
import com.example.maun.dto.UserResponse;
import com.example.maun.entity.Phone;
import com.example.maun.entity.User;
import com.example.maun.repository.PhoneRepository;
import com.example.maun.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public interface UserService {
    NewUserResponse registerUser(NewUser newUser);
    UserResponse buscarPorId(UUID id, String token);
}