package com.project.thymeleafboard.exception;

import lombok.Getter;

@Getter
public class ResourcePermissionDeniedException extends RuntimeException {
    private Integer id;

    public ResourcePermissionDeniedException(String message, Integer id) {
        super(message);
        this.id = id;
    }

    public ResourcePermissionDeniedException(String message) {
        super(message);
    }

}
