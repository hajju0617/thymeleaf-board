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
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException, ServletException {
        String ajaxHeader = httpServletRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxHeader)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);                // 상태코드 401.
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write("{\"error\": \"로그인이 필요해요.\"}");     // 큰따옴표(")를 이스케이프 처리.
        } else {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE);
        }
    }
}
