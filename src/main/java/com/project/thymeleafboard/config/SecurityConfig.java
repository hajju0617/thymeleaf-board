package com.project.thymeleafboard.config;

import com.project.thymeleafboard.security.CustomAuthenticationEntryPoint;
import com.project.thymeleafboard.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        (authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers("/article/create", "/comment/create/**",
                                                 "/article/vote/**", "/comment/vote/**",
                                                 "/article/modify/**", "comment/modify/**").authenticated()
                                .anyRequest().permitAll())
                .formLogin((formLogin -> formLogin.loginPage("/user/login")
                        .defaultSuccessUrl("/", false)))
                .logout((logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/article/list?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-user")))
                .rememberMe((rememberMe -> rememberMe.key("uniqueAndSecretKey")
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-user")
                        .tokenValiditySeconds(60 * 60 * 24 * 7)        // 24h * 7 = 1주일.
                        .userDetailsService(customUserDetailsService)))
                .exceptionHandling((exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint)));
        return httpSecurity.build();
    }

    // PasswordEncoder 빈 등록. (BCryptPasswordEncoder : PasswordEncoder 구현체)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 빈 등록. (AuthenticationManager : 시큐리티의 인증을 처리함)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
