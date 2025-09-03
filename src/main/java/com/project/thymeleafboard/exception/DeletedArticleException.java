package com.project.thymeleafboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Deleted Article")
public class DeletedArticleException extends RuntimeException {
    public DeletedArticleException(String message) {
        super(message);
    }
}
