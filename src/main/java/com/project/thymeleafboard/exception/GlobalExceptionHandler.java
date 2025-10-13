package com.project.thymeleafboard.exception;



import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handle404(NoResourceFoundException ex, HttpServletRequest httpServletRequest) {      // 매핑되어 있지않은 URL로 요청이 올 경우 그 예외를 잡음.
        log.warn("존재하지 않는 URL로 요청 : msg = {}, url = {}", ex.getMessage(), httpServletRequest.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException dnfe,
                                              HttpServletRequest httpServletRequest,
                                              RedirectAttributes redirectAttributes) {
        log.warn("존재하지 않는 데이터 요청 : mse = {}, url = {}, query = {}", dnfe.getMessage(), httpServletRequest.getRequestURI(), httpServletRequest.getQueryString());
        redirectAttributes.addFlashAttribute("errorMsg", dnfe.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(InvalidPageException.class)
    public String handleInvalidArticlePageException(HttpServletRequest httpServletRequest,
                                                    InvalidPageException ipe,
                                                    RedirectAttributes redirectAttributes) {
        if (ipe.getArticleId() != null) {
            log.warn("존재하지 않는 comment 페이지 요청 : msg = {}, url = {}, query = {}, page = {}",
                    ipe.getMessage(), httpServletRequest.getRequestURI(), httpServletRequest.getQueryString(), ipe.getPage());
            redirectAttributes.addFlashAttribute("errorMsg", ipe.getMessage());
            return "redirect:/article/detail/" + ipe.getArticleId() + "?page=" + ipe.getPage();

        } else {
            log.warn("존재하지 않는 article 페이지 요청 : msg = {}, url = {}, query = {}, page = {}",
                    ipe.getMessage(), httpServletRequest.getRequestURI(), httpServletRequest.getQueryString(), ipe.getPage());
            redirectAttributes.addFlashAttribute("errorMsg", ipe.getMessage());
            return "redirect:/article/list";
        }
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<String> handleMailSendException(MailSendException mse) {
        log.warn("메일 발송시 에러 발생 : {}", mse.getMessage());
        log.error("메일 발송 에러의 원인 : ", mse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("죄송합니다. 서버 오류로 인해 메일 발송이 실패했어요.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException unfe, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", unfe.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public String handleAlreadyLoggedInException(AlreadyLoggedInException alie, RedirectAttributes redirectAttributes) {
        log.warn("로그인 되어 있는 상태에서 로그인 페이지 요청 : {}", alie.getMessage());
        redirectAttributes.addFlashAttribute("errorMsg", alie.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(InvalidValueException.class)
    public String handleInvalidValueException(InvalidValueException ive, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        log.warn("유효하지 않은 요청 : msg = {}, url = {}, query = {}",
                ive.getMessage(), httpServletRequest.getRequestURI(), httpServletRequest.getQueryString());
        redirectAttributes.addFlashAttribute("errorMsg", ive.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "로그인이 필요해요.");
        return "redirect:/user/login";
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public String handleAccessDenied(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "접근 권한이 없어요.");
        return "redirect:/article/list";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "잘못된 요청이에요.");
        return "redirect:/article/list";
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest httpServletRequest) {
        log.error("예상치 못한 에러 발생 : msg = {}, url : {}, query : {}",
                e.getMessage(), httpServletRequest.getRequestURI(), httpServletRequest.getQueryString());
        log.error("에러의 원인 : ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
