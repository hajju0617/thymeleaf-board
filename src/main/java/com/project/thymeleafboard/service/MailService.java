package com.project.thymeleafboard.service;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.dto.SendMailDto;
import com.project.thymeleafboard.entity.Mail;
import com.project.thymeleafboard.exception.MailSendException;
import com.project.thymeleafboard.repository.MailRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.FROM_EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;

    @Transactional
    public void sendVerificationMail(SendMailDto sendMailDto) {
        String toMail = sendMailDto.getEmail();
        String title = "[인증번호] 회원가입을 위한 인증번호입니다.";
        String authNumber = CommonUtil.makeRandomNumber();
        String content =
                "안녕하세요." + 	//html 형식
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "해당 인증번호를 입력해주세요";

        Optional<Mail> existingMail = mailRepository.findByEmail(toMail);
        Mail mail;
        if (existingMail.isPresent()) {
            mail = existingMail.get();
            mail.updateAuthNum(authNumber);
        } else {
            mail = Mail.create(toMail, authNumber);
        }
        mailRepository.save(mail);
        mailSend(FROM_EMAIL, toMail, title, content);
    }

    @Transactional
    public void sendTempPasswordMail(String toMail, String tempPassword) {
        String title = "[임시 비밀번호] 임시 비밀번호가 발급되었어요.";
        String content =
                "안녕하세요. 요청하신 임시 비밀번호가 발급되었습니다." +
                        "<br>" +
                        "임시 비밀번호: " + tempPassword +
                        "<br><br>" +
                        "임시 비밀번호로 로그인 후, 반드시 새로운 비밀번호로 변경해 주세요.";
        mailSend(FROM_EMAIL, toMail, title, content);
    }

    @Transactional
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new MailSendException("메일 전송 실패 : " + toMail, e);
        }
    }

    @Transactional
    public boolean checkVerificationCode(String email, String authNum) {
        mailRepository.deleteExpiredAuthNums(LocalDateTime.now().minusMinutes(2));  // 현재 시각에서 2분을 뺀 시각
        Optional<Mail> optionalMail = mailRepository.findByEmailAndAuthNum(email, authNum);
        return optionalMail.isPresent();
    }
}
