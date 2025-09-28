package com.project.thymeleafboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.QueryTypeMismatchException;
import org.springframework.orm.jpa.JpaSystemException;

@Getter
@Setter
public class VerificationMailDto {
    private String email;
    private String authNum;
}
