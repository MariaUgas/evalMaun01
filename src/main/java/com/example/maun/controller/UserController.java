package com.example.maun.controller;

import com.example.maun.dto.UserDto;
import com.example.maun.entity.User;
import com.example.maun.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping( consumes=MediaType.APPLICATION_JSON_VALUE , produces=MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserDto> registrarUsuario(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE , path ="{userId}")
    public ResponseEntity<User>  buscarUsuarios(@PathVariable("userId") UUID id){
        Optional<User> user_ = userService.buscarPorId(id);

        if(user_ != null && !user_.isEmpty())
            return ResponseEntity.ok(user_.get());

        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception exception, HttpServletRequest request){

        Map<String, String> apiError = new HashMap<>();
        apiError.put("message",exception.getMessage());
        apiError.put("timestamp", new Date().toString());
        apiError.put("url", request.getRequestURL().toString());
        apiError.put("http-method", request.getMethod());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if(exception instanceof AccessDeniedException){
            status = HttpStatus.FORBIDDEN;
        }
        if(exception instanceof RuntimeException){
            status = HttpStatus.BAD_REQUEST;
        }


        return ResponseEntity.status(status).body(apiError);
    }
}
