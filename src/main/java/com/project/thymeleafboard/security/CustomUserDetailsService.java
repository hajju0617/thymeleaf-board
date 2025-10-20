package com.project.thymeleafboard.security;

import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.ERROR_USER_NOT_FOUND_BY_USERNAME;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);

        if (optionalSiteUser.isEmpty()) {
            throw new UsernameNotFoundException(ERROR_USER_NOT_FOUND_BY_USERNAME);
        }
        return new CustomUserDetails(optionalSiteUser.get());
    }
}
