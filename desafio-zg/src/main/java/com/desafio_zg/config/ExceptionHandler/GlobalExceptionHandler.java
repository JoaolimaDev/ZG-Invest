package com.desafio_zg.config.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<HashMap<String,String>> HandleValidationExceptions() {

        HashMap<String, String> response = new HashMap<>();

        response.put("Mensagem","Por favor, envie um formato de data válido!");
        response.put("Status", HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
}
