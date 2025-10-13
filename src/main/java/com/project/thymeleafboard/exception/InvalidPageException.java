package com.project.thymeleafboard.exception;

import lombok.Getter;

@Getter
public class InvalidPageException extends RuntimeException {

    private Integer articleId;
    private int page;

    public InvalidPageException(String message, Integer articleId, int page) {
        super(message);
        this.articleId = articleId;
        this.page = page;
    }

    public InvalidPageException(String message) {
        super(message);
    }
}
