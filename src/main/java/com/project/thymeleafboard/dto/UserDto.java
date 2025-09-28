package com.project.thymeleafboard.dto;


import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


import static com.project.thymeleafboard.common.GlobalConst.*;

@Getter
@Setter
public class UserDto {
    @Pattern(regexp = "^[a-z0-9]{5,15}$", message = USERNAME_REQUIRED)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*])\\S{8,20}$", message = PW_REQUIRED)
    private String password;
    private String passwordConfirm;

    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}@[a-z]{3,7}\\.(com|net){1}$", message = EMAIL_REQUIRED)
    private String email;

    private String authNum;
}
