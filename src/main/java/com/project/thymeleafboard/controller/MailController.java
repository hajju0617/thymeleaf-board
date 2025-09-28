package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.SendMailDto;
import com.project.thymeleafboard.dto.VerificationMailDto;
import com.project.thymeleafboard.service.MailService;
import com.project.thymeleafboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.thymeleafboard.common.GlobalConst.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {
    private final UserService userService;
    private final MailService mailService;

    /*
        인증번호 전송 메서드.

        @Param
        sendMailDto : 이메일.
    */
    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationCode(@RequestBody SendMailDto sendMailDto) {
        try {
            if (userService.findByEmail(sendMailDto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(EMAIL_ALREADY_REGISTERED);
            } else {
                mailService.sendVerificationMail(sendMailDto);
                return ResponseEntity.status(HttpStatus.OK).body(VERIFICATION_CODE_SENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SERVER_ERROR);
        }
    }

    /*
        인증번호 검증 메서드.

        @Param
        verificationMailDto : 이메일, 인증번호.
    */
    @PostMapping("/verification-code")
    public ResponseEntity<String> checkVerificationCode(@RequestBody VerificationMailDto verificationMailDto) {
        try {
            if (mailService.checkVerificationCode(verificationMailDto.getEmail(), verificationMailDto.getAuthNum())) {
                return ResponseEntity.status(HttpStatus.OK).body(EMAIL_VERIFICATION_SUCCESS);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EMAIL_VERIFICATION_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SERVER_ERROR);
        }
    }
}
