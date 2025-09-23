package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.project.thymeleafboard.common.GlobalConst.*;

@Getter
@Setter
@ToString
public class UserDto {
    @Size(min = 3, max = 15)
    @Pattern(regexp = "^[a-z0-9]{3,15}$", message = ID_REQUIRED)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*])\\S{8,20}$", message = PW_REQUIRED)
    private String password;
    private String passwordConfirm;

    @Email
    @NotBlank(message = EMAIL_REQUIRED)
    private String email;
}
