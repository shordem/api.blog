package com.shordem.blog.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class JwtErrorAdvice {

    @ExceptionHandler(value = { JwtException.class, SignatureException.class })
    public ResponseEntity<String> handleJwtException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}
