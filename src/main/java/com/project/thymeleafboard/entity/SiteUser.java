package com.project.thymeleafboard.entity;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.security.SignUpProviderType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpProviderType signUpProviderType;

    public SiteUser() {
    }

    private SiteUser(String username, String password, String email, SignUpProviderType signUpProviderType, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createDate = LocalDateTime.now();
        this.role = userRole;
        this.signUpProviderType = SignUpProviderType.GOOGLE;
    }

    private SiteUser(String username, String password, String email) {
        this(username,
            password,
            email,
            SignUpProviderType.LOCAL,
            "admin".equals(username) ? UserRole.ADMIN : UserRole.USER);
    }

    private SiteUser(String username, String password, String email, SignUpProviderType signUpProviderType) {
        this(username, password, email, signUpProviderType, UserRole.USER);
    }

    public static SiteUser create(UserDto userDto, String password) {
        return new SiteUser(userDto.getUsername(), password, userDto.getEmail());
    }

    public static SiteUser createOauth2User(String username, String password, String email, SignUpProviderType signUpProviderType) {
        return new SiteUser(username, password, email, signUpProviderType);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.modifyDate = LocalDateTime.now();
    }
}
