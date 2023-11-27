package com.example.maun.service;

import com.example.maun.dto.UserDto;
import com.example.maun.entity.Phone;
import com.example.maun.entity.User;
import com.example.maun.repository.PhoneRepository;
import com.example.maun.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private JwtService jwtService;

    public UserDto registerUser(User user) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (optionalUser.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");

        } else {
            user.setCreated(LocalDateTime.now());
            user.setModified(user.getCreated());
            user.setIsactive(true);
            String jwt = jwtService.generateToken(user,generateExtraClaims(user));
            user.setUsertoken(jwt);
            User user1=userRepository.saveAndFlush(user);
            if(!user.getPhones().isEmpty()) {
                user.getPhones().forEach(phone -> phone.getUser().setId(user1.getId()));
                phoneRepository.saveAllAndFlush(user.getPhones());
            }
            return new UserDto(user.getId(),user.getCreated(),user.getModified(),user1.getModified(),jwt,user1.getIsactive());
        }
    }

    private Map<String, Object> generateExtraClaims(User user) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("password", user.getPassword());

        return extraClaims;
    }

    public Optional<User> buscarPorId(UUID id) {
        return userRepository.findById(id);
    }
}