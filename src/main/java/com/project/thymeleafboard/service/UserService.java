package com.project.thymeleafboard.service;

import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.AlreadyLoggedInException;
import com.project.thymeleafboard.exception.UserNotFoundException;
import com.project.thymeleafboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.UnknownServiceException;
import java.security.Principal;
import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.ERROR_ALREADY_LOGGED_IN;
import static com.project.thymeleafboard.common.GlobalConst.ERROR_USER_NOT_FOUND_BY_USERNAME;

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

    public SiteUser findByUsernameOrThrow(String username) {
        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);
        if (optionalSiteUser.isPresent()) {
            return optionalSiteUser.get();
        } else {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND_BY_USERNAME);
        }
    }
    public Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<SiteUser> findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void changePassword(SiteUser siteUser, String tempPassword) {
        siteUser.updatePassword(encodePassword(tempPassword));
        userRepository.save(siteUser);
    }

    public void isLoggedIn(Principal principal) {
        if (principal != null) {
            throw new AlreadyLoggedInException(String.format(ERROR_ALREADY_LOGGED_IN, principal.getName()));
        }
    }
}
