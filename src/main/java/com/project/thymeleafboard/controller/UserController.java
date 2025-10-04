package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.dto.FindIdDto;
import com.project.thymeleafboard.dto.FindPwDto;
import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.service.MailService;
import com.project.thymeleafboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.QueryTypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    /*
        회원가입 뷰 페이지 메서드.

        @Param
        userDto : signup_form 뷰 페이지에서 th:object 에서 사용.
    */
    @GetMapping("/signup")
    public String signup(UserDto userDto) {
        return "signup_form";
    }

    /*
        회원가입 처리 메서드.

        @Param
        UserDto : 회원가입을 요청한 데이터.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
    */
    @PostMapping("/signup")
        public String signup(@Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!mailService.checkVerificationCode(userDto.getEmail(), userDto.getAuthNum())) {
            bindingResult.rejectValue("authNum", "invalidAuthNum", EMAIL_VERIFICATION_FAILED);
            return "signup_form";
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            // bindingResult.rejectValue("필드명", "에러 코드", "에러 메시지")
            bindingResult.rejectValue("passwordConfirm", "passwordIncorrect", PASSWORD_INCORRECT);
            return "signup_form";
        }
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DuplicateUsername", USERNAME_ALREADY_REGISTERED);
            return "signup_form";
        }
        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DuplicateEmail", EMAIL_ALREADY_REGISTERED);
            return "signup_form";
        }
        try {
            userService.createUser(userDto);
            redirectAttributes.addFlashAttribute("successMsg", SUCCESS_SIGNUP);
        } catch (Exception e) {
            log.warn("회원가입시 에러 발생 : {}", e.getMessage());
            log.warn("회원가입시 에러 원인 : ", e);
            bindingResult.reject("signupError", e.getMessage());
            return "signup_form";
        }
        return "redirect:/article/list";
    }

    /*
        아이디 중복체크 처리 메서드
    */
    @GetMapping("/check-username")
    @ResponseBody
    public boolean duplicateCheckUsername(@RequestParam("username") String username) {
        return userService.findByUsername(username).isEmpty();
    }

    /*
        로그인 뷰 페이지 메서드.
    */
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    /*
        아이디 찾기 뷰 페이지 메서드.

        @Param
        findIdDto : 뷰 페이지에서 th:object 에서 사용.
    */
    @GetMapping("/find-id")
    public String findId(FindIdDto findIdDto) {
        return "find_id";
    }

    /*
        아이디 찾기 처리 메서드.

        @Param
        findIdDto : 아이디 찾으려고 요청한 이메일 데이터.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        model : 뷰템플릿에서 모델객체 활용.
    */
    @PostMapping("/find-id")
    public String findId(@Valid FindIdDto findIdDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "find_id";
        }
        Optional<SiteUser> optionalSiteUser = userService.findByEmail(findIdDto.getEmail());
        if (optionalSiteUser.isEmpty()) {
            bindingResult.rejectValue("email", "emailNotFound", EMAIL_NOT_FOUND);
        } else {
            String maskedUsername = CommonUtil.maskUsername(optionalSiteUser.get().getUsername());
            model.addAttribute("username", maskedUsername);
        }
        return "find_id";
    }

    /*
         비밀번호 찾기(임시 비밀번호 발급) 뷰 페이지 메서드.

         @Param
         findPwDto : 뷰 페이지에서 th:object 에서 사용.
    */
    @GetMapping("/find-pw")
    public String findPw(FindPwDto findPwDto) {
        return "find_pw";
    }

    /*
        비밀번호 찾기(임시 비밀번호 발급) 처리 메서드.

        @Param
        findPwDto : 사용자 이메일과 아이디를 받음.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        RedirectAttributes : 리다이렉트(Redirect)시 데이터를 전달하는 역할.
    */
    @PostMapping("/find-pw")
    public String findPw(@Valid FindPwDto findPwDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "find_pw";
        }
        Optional<SiteUser> optionalSiteUser = userService.findByUsernameAndEmail(findPwDto.getUsername(), findPwDto.getEmail());
        if (optionalSiteUser.isPresent()) {
            String tempPassword = CommonUtil.makeRandomPassword();
            mailService.sendTempPasswordMail(findPwDto.getEmail(), tempPassword);
            userService.changePassword(optionalSiteUser.get(), tempPassword);
            redirectAttributes.addFlashAttribute("successMsg", SUCCESS_TEMP_PASSWORD_SENT_EMAIL);
        } else {
            bindingResult.reject("userNotFound", USER_NOT_FOUND_BY_USERNAME_AND_EMAIL);
            return "find_pw";
        }
        return "redirect:/user/login";
    }
}