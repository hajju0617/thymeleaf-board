package com.project.thymeleafboard.security;

import com.project.thymeleafboard.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final SiteUser siteUser;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(siteUser.getRole().getValue()));
    }

    @Override
    public String getPassword() {
        return siteUser.getPassword();
    }

    @Override
    public String getUsername() {
        return siteUser.getUsername();
    }
}
