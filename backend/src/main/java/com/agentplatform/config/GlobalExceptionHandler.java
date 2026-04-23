package com.agentplatform.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("error", ex.getClass().getSimpleName());
        error.put("message", ex.getMessage());

        ex.printStackTrace(); // keep for dev, remove in prod later

        return ResponseEntity
                .status(500)
                .body(error);
    }
}