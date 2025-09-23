package com.project.thymeleafboard.security;

import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);

        if (optionalSiteUser.isEmpty()) {
            throw new UsernameNotFoundException("해당 사용자 이름(ID)을 찾을 수 없어요.");
        }
        return new CustomUserDetails(optionalSiteUser.get());
    }
}
