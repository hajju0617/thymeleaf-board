package com.project.thymeleafboard.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends RuntimeException {
    private Integer articleId;
    private int page;

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Integer articleId, int page) {
        super(message);
        this.articleId = articleId;
        this.page = page;
    }
}
