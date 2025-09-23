package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.EMAIL_REQUIRED;

@Getter
@Setter
public class FindIdDto {
    @Email
    @NotBlank(message = EMAIL_REQUIRED)
    private String email;
}
