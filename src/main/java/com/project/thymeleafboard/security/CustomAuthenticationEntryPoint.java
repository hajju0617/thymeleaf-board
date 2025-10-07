package com.project.thymeleafboard.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final String LOGIN_PAGE = "/user/login";
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ajaxHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);                // 상태코드 401.
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"로그인이 필요해요.\"}");     // 큰따옴표(")를 이스케이프 처리.
        } else {
            response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
        }
    }
}
