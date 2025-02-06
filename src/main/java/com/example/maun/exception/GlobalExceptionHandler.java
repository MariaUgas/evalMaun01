package com.example.maun.exception;

import javax.servlet.http.HttpServletRequest;

import com.example.maun.dto.ErrorList;
import com.example.maun.dto.ErrorResponse;
import com.example.maun.dto.PhoneUser;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorList> handleValidationErrors(MethodArgumentNotValidException ex) {
        ErrorList errores= new ErrorList();
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        errores.setErrores(errors.stream()
                .map(er -> new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),er))
                .collect(Collectors.toList()));
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorList> handleGenericException(Exception exception, HttpServletRequest request) {
        HttpStatus status = determineHttpStatus(exception);
        ErrorList errores= new ErrorList();
        errores.setError(new ErrorResponse(LocalDateTime.now(),status.value(),exception.getMessage()));
        return ResponseEntity.status(status).body(errores);
    }

    private HttpStatus determineHttpStatus(Exception exception){
        if (exception instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        if (exception instanceof ResponseStatusException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (exception instanceof RuntimeException) {
            return HttpStatus.BAD_REQUEST;
        }



        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
