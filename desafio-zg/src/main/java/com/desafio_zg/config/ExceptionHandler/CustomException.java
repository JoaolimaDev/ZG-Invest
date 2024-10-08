package com.desafio_zg.config.ExceptionHandler;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/***
 * Metódo criado para exceções customizavéis com mensagem, e status HTTP
 ***/
@Setter
@Getter
public class CustomException extends RuntimeException {
    
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status; 
    }
}