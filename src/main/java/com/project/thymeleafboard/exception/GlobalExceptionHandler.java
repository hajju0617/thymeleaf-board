package com.project.thymeleafboard.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handle404(NoResourceFoundException ex) {      // 매핑되어 있지않은 URL로 요청이 올 경우 그 예외를 잡음.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
