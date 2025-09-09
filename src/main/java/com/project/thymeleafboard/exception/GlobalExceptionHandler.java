package com.project.thymeleafboard.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handle404(NoResourceFoundException ex) {      // 매핑되어 있지않은 URL로 요청이 올 경우 그 예외를 잡음.
        log.warn("존재하지 않는 URL로 요청 : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException dnfe) {
        log.warn("존재하지 않는 게시글 요청 : {}", dnfe.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(DeletedArticleException.class)
    public String handleDeletedArticleException(DeletedArticleException dae) {
        log.warn("삭제된 게시글 요청 : {}", dae.getMessage());
        return "redirect:/article/list";
    }
}
