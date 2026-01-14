package com.ensta.myfilmlist.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ensta.myfilmlist.exception.ControllerException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<String> handleControllerException(ControllerException exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Erreur : " + exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleValidationException(BindException exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Erreur de validation : " + exception.getAllErrors().get(0).getDefaultMessage());
    }
}
