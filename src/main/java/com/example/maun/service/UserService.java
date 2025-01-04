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
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public NewUserResponse registerUser(NewUser newUser) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(newUser.getEmail()));
        if (optionalUser.isPresent()) {
            log.error("El correo ya está registrado, email invalido: {}",newUser.getEmail());
            throw new RuntimeException("El correo ya está registrado");

        } else {
            User user=userRepository.saveAndFlush(new User(null,newUser.getName(),newUser.getEmail(), newUser.getPassword()));
            String jwt = jwtService.generateToken(user,generateExtraClaims(user));
            log.info("Datos del usuario creado , id:{}",user.getId());
            jwtService.saveToken( user.getId().toString(),   jwt    );
            if(!newUser.getPhones().isEmpty()) {
                log.info("Datos del telefono del usuario :{} ",user.getPhones().toString());
                List<Phone> phones = newUser.getPhones().stream()
                        .map(pu -> new Phone(null, pu.getNumber(), pu.getCitycode(), pu.getCountrycode(), user))
                        .collect(Collectors.toList());
                phoneRepository.saveAllAndFlush(phones);
            }
            return new NewUserResponse(
                    user.getId(),
                    user.getCreated(),
                    user.getModified(),
                    jwt,
                    user.getIsactive());
        }
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("password", user.getPassword());

        return extraClaims;
    }

    public UserResponse buscarPorId(UUID id, String token) {
        if(token.isEmpty() || !token.equals(jwtService.getToken(id.toString()))){
            log.error("Token inválido, id: {}",id);
            throw new RuntimeException("Token inválido");
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user=optionalUser.get();
            List<PhoneUser> phones = user.getPhones().stream()
                    .map(pu -> new PhoneUser(pu.getNumber(), pu.getCitycode(), pu.getCountrycode()))
                    .collect(Collectors.toList());
            return new UserResponse(
                    user.getId(),
                    user.getCreated(),
                    user.getModified(),
                    jwtService.getToken(id.toString()),
                    user.getIsactive(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    phones);
        } else {
            log.error("No existe un usuario con el identificador ingresado, id: {}",id);
            throw new RuntimeException("No existe un usuario con el identificador ingresado");
        }
    }
}