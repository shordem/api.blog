package com.shordem.blog.advice;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsernameNotFoundAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> body = Map.of("message", ex.getMessage());

        return ResponseEntity.status(Response.SC_NOT_FOUND).body(body);
    }
}
