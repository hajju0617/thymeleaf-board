package com.project.thymeleafboard.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String authNum;

    private LocalDateTime createDate;

    public Mail() {
    }

    private Mail(String email, String authNum) {
        this.email = email;
        this.authNum = authNum;
        this.createDate = LocalDateTime.now();
    }

    public static Mail create(String email, String authNum) {
        return new Mail(email, authNum);
    }

    public void updateAuthNum(String authNum) {
        this.authNum = authNum;
        this.createDate = LocalDateTime.now();
    }
}
