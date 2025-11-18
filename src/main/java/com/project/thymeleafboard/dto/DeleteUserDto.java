package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.CONTENT_REQUIRED;

@Setter
@Getter
public class DeleteUserDto {

    private String password;

    @NotBlank(message = CONTENT_REQUIRED)
    private String message;
}
