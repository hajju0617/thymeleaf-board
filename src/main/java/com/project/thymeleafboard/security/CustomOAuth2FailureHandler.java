package com.project.thymeleafboard.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if (exception instanceof OAuth2AuthenticationException &&
                "email_exists".equals(((OAuth2AuthenticationException) exception).getError().getErrorCode())) {

            // 로그인 페이지로 리다이렉트하면서 에러 메시지 전달
            response.sendRedirect("/user/login?error=email_exists");
        } else {
            response.sendRedirect("/user/login?error");
        }
    }
}
