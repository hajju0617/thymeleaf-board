package com.project.thymeleafboard.entity;

import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.security.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public SiteUser() {
    }

    private SiteUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createDate = LocalDateTime.now();
        if (!username.equals("admin")) {
            this.role = UserRole.USER;
        } else {
            this.role = UserRole.ADMIN;
        }
    }

    public static SiteUser create(UserDto userDto, String password) {
        return new SiteUser(userDto.getUsername(), password, userDto.getEmail());
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
