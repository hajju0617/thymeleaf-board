package com.project.thymeleafboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MailSendException extends RuntimeException {
    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
