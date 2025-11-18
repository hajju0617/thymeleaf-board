package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.*;

@Setter
@Getter
public class ChangePwDto {
    @NotBlank(message = CURRENT_PASSWORD)
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*])\\S{8,20}$", message = PW_REQUIRED)
    private String newPassword;
    private String confirmPassword;
}
