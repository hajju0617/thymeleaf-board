package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.EMAIL_REQUIRED;
import static com.project.thymeleafboard.common.GlobalConst.USERNAME_REQUIRED;

@Getter
@Setter
public class FindPwDto {
    @NotBlank(message = USERNAME_REQUIRED)
    private String username;
    @NotBlank(message = EMAIL_REQUIRED)
    private String email;
}
