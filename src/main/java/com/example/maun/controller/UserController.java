package com.example.maun.controller;

import com.example.maun.dto.NewUser;
import com.example.maun.dto.NewUserResponse;
import com.example.maun.dto.UserResponse;
import com.example.maun.service.UserService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@RestController
@RequestMapping("/api/v2")
@Validated
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path ="/sign-up", consumes=MediaType.APPLICATION_JSON_VALUE , produces=MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<NewUserResponse> registrarUsuario(@Valid @RequestBody NewUser user){
        try {
            NewUserResponse userResponse = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (RuntimeException e) {
                throw e;
        }
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE , path ="/login/{userId}")
    public ResponseEntity<UserResponse>  buscarUsuarios(@PathVariable("userId") UUID id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No tiene autorizacion");
            }
            String token = authorizationHeader.substring(7);
            log.info("token: {}",token);

            UserResponse userResponse = userService.buscarPorId(id,token);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (RuntimeException e) {
            throw e;
        }
    }

}
