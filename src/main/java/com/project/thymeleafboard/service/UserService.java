package com.project.thymeleafboard.service;

import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserDto userDto) {
        SiteUser user = SiteUser.create(userDto, encodePassword(userDto.getPassword()));
        userRepository.save(user);
    }

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<SiteUser> findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void changePassword(SiteUser siteUser, String tempPassword) {
        siteUser.updatePassword(encodePassword(tempPassword));
        userRepository.save(siteUser);
    }
}
