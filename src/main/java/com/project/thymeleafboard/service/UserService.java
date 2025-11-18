package com.project.thymeleafboard.service;

import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.AlreadyLoggedInException;
import com.project.thymeleafboard.exception.UserNotFoundException;
import com.project.thymeleafboard.repository.ArticleRepository;
import com.project.thymeleafboard.repository.CommentRepository;
import com.project.thymeleafboard.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Transactional
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
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }
    }
    public Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<SiteUser> findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    @Transactional
    public void changePassword(SiteUser siteUser, String newPassword) {
        siteUser.updatePassword(encodePassword(newPassword));
        userRepository.save(siteUser);
    }
    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void isLoggedIn(Principal principal) {
        if (principal != null) {
            throw new AlreadyLoggedInException(String.format(ERROR_ALREADY_LOGGED_IN, principal.getName()));
        }
    }

    public boolean isPasswordIncorrect(String rawNewPassword, String storedEncodedPassword) {
        return !passwordEncoder.matches(rawNewPassword, storedEncodedPassword);
    }

    public boolean isDeleteMessageIncorrect(String message) {
        return !USER_DELETE_MESSAGE.equals(message.trim());
    }

    public boolean isRegisteredForLessThan24(SiteUser siteUser) {
        return LocalDateTime.now().isBefore(siteUser.getCreateDate().plusHours(24));
    }

    @Transactional
    public void deleteUser(SiteUser siteUser) {
        List<Article> votedArticles = articleRepository.findAllByVoter(siteUser);
        for (Article article : votedArticles) {
            article.getVoter().remove(siteUser);
        }
        List<Comment> votedComment = commentRepository.findAllByVoter(siteUser);
        for (Comment comment : votedComment) {
            comment.getVoter().remove(siteUser);
        }
        commentRepository.deleteAllByAuthor(siteUser);
        articleRepository.deleteAllByAuthor(siteUser);
        userRepository.delete(siteUser);
    }

    public void clearSessionAndCookies(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
            Cookie[] cookies = httpServletRequest.getCookies();
            for (Cookie cookie : cookies) {
                if ("remember-user".equals(cookie.getName())) {
                    Cookie deleteCookie = new Cookie("remember-user", null);
                    deleteCookie.setMaxAge(0);
                    deleteCookie.setPath("/");
                    httpServletResponse.addCookie(deleteCookie);
                    break;
                }
            }
        }
    }
}
