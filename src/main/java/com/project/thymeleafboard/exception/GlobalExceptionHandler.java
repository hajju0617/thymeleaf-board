package com.project.thymeleafboard.exception;



import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handle404(NoResourceFoundException ex) {      // 매핑되어 있지않은 URL로 요청이 올 경우 그 예외를 잡음.
        log.warn("존재하지 않는 URL로 요청 : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException dnfe) {
        log.warn("존재하지 않는 게시글 요청 : {}", dnfe.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(DeletedArticleException.class)
    public String handleDeletedArticleException(DeletedArticleException dae) {
        log.warn("삭제된 게시글 요청 : {}", dae.getMessage());
        return "redirect:/article/list";
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<String> handleMailSendException(MailSendException mse) {
        log.warn("메일 발송시 에러 발생 : {}", mse.getMessage());
        log.error("메일 발송 에러의 원인 : ", mse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 메일 발송이 실패했어요.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException unfe, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", unfe.getMessage());
        return "redirect:/login";
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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("예상치 못한 에러 발생 : {}", e.getMessage());
        log.error("에러의 원인 : ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
