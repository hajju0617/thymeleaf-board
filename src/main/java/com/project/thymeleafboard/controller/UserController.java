package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.dto.*;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.OAuthPasswordChangeException;
import com.project.thymeleafboard.service.MailService;
import com.project.thymeleafboard.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
        principal : 로그인이 되어 있는지 확인.
    */
    @GetMapping("/signup")
    public String signup(UserDto userDto, Principal principal) {
        userService.isLoggedIn(principal);
        return "signup_form";
    }

    /*
        회원가입 처리 메서드.

        @Param
        UserDto : 회원가입을 요청한 데이터.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        RedirectAttributes : 리다이렉트(Redirect)시 데이터를 전달하는 역할.
    */
    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!mailService.checkVerificationCode(userDto.getEmail(), userDto.getAuthNum())) {
            bindingResult.rejectValue("authNum", "invalidAuthNum", ERROR_EMAIL_VERIFICATION);
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
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, SUCCESS_SIGNUP);
        } catch (Exception e) {
            log.warn("회원가입시 에러 발생 : msg = {}, cause = ", e.getMessage(), e);
            bindingResult.reject("signupError", e.getMessage());
            return "signup_form";
        }
        return "redirect:/article/list";
    }

    /*
        아이디 중복체크 처리 메서드

        @Param
        username : 중복 체크할 아이디.
    */
    @GetMapping("/check-username")
    @ResponseBody
    public boolean duplicateCheckUsername(@RequestParam("username") String username) {
        return userService.findByUsername(username).isEmpty();
    }

    /*
        로그인 뷰 페이지 메서드.

        @Param
        principal : 로그인이 되어 있는지 확인.
    */
    @GetMapping("/login")
    public String login(Principal principal) {
        userService.isLoggedIn(principal);
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
        if (optionalSiteUser.isPresent()) {
            SiteUser siteUser = optionalSiteUser.get();
            if ("GOOGLE".equals(siteUser.getSignUpProviderType().name())) {
                bindingResult.reject("oauth2IdFindNotAllowed", ERROR_OAUTH2_FIND_ID);
                return "find_id";
            }
            String maskedUsername = CommonUtil.maskUsername(optionalSiteUser.get().getUsername());
            model.addAttribute("username", maskedUsername);
        } else {
            bindingResult.rejectValue("email", "emailNotFound", EMAIL_NOT_FOUND);
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
            SiteUser siteUser = optionalSiteUser.get();
            if ("GOOGLE".equals(siteUser.getSignUpProviderType().name())) {
                bindingResult.reject("oAuth2PasswordResetNotAllowed", ERROR_OAUTH2_PASSWORD_RESET);
                return "find_pw";
            }
            String tempPassword = CommonUtil.makeRandomPassword();
            mailService.sendTempPasswordMail(findPwDto.getEmail(), tempPassword);
            userService.changePassword(siteUser, tempPassword);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, SUCCESS_TEMP_PASSWORD_SENT_EMAIL);
        } else {
            bindingResult.reject("userNotFound", ERROR_USER_NOT_FOUND_BY_USERNAME_AND_EMAIL);
            return "find_pw";
        }
        return "redirect:/user/login";
    }

    /*
        마이페이지 처리 메서드.

         @Param
         principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */
    @GetMapping("/info")
    public String myPage(Model model, Principal principal) {
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        model.addAttribute("user", siteUser);
        return "user_info";
    }

    /*
         비밀번호 변경 뷰 페이지 메서드.

         @Param
         changePwDto : 뷰 페이지에서 th:object 에서 사용.
         principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */
    @GetMapping("/password/change")
    public String changePassword(ChangePwDto changePwDto, Principal principal) {
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        if ("GOOGLE".equals(siteUser.getSignUpProviderType().name())) {
            throw new OAuthPasswordChangeException(ERROR_OAUTH2_PASSWORD_CHANGE);
        }
        return "change_pw_form";
    }

    /*
         비밀번호 변경 처리 메서드.

         @Param
         changePwDto : 사용자가 입력한 데이터.
         RedirectAttributes : 리다이렉트(Redirect)시 데이터를 전달하는 역할.
         principal : 현재 인증된 사용자(로그인한 사용자) 객체.
         BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
    */
    @PostMapping("/password/change")
    public String changePassword(@Valid ChangePwDto changePwDto, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "change_pw_form";
        }
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        if (userService.isPasswordIncorrect(changePwDto.getCurrentPassword(), siteUser.getPassword())) {
            bindingResult.reject("currentPasswordIncorrect", CURRENT_PASSWORD_INCORRECT);
            return "change_pw_form";
        } else if (!changePwDto.getNewPassword().equals(changePwDto.getConfirmPassword())) {
            bindingResult.reject("passwordIncorrect", PASSWORD_INCORRECT);
            return "change_pw_form";
        } else {
            userService.changePassword(siteUser, changePwDto.getNewPassword());
        }
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, SUCCESS_PASSWORD_CHANGE);
        return "redirect:/user/info";
    }

    /*
         회원 탈퇴 뷰 페이지 메서드.
    
         @Param
         deleteUserDto : 뷰 페이지에서 th:object 에서 사용.
         principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */
    @GetMapping("/delete")
    public String deleteUser(Model model, DeleteUserDto deleteUserDto, Principal principal) {
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        boolean isLocalUser = "LOCAL".equals(siteUser.getSignUpProviderType().name());
        model.addAttribute("isLocalUser", isLocalUser);
        return "user_delete_confirm";
    }

    /*
         회원 탈퇴 처리 메서드.

         @Param
         deleteUserDto : 사용자가 입력한 데이터.
         RedirectAttributes : 리다이렉트(Redirect)시 데이터를 전달하는 역할.
         principal : 현재 인증된 사용자(로그인한 사용자) 객체.
         BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
    */
    @PostMapping("/delete")
    public String deleteUser(@Valid DeleteUserDto deleteUserDto, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Principal principal,
                             HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (bindingResult.hasErrors()) {
            return "user_delete_confirm";
        }
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        if ("LOCAL".equals(siteUser.getSignUpProviderType().name()) && userService.isPasswordIncorrect(deleteUserDto.getPassword(), siteUser.getPassword())) {
            bindingResult.reject("currentPasswordIncorrect", CURRENT_PASSWORD_INCORRECT);
            return "user_delete_confirm";
        } else if (userService.isDeleteMessageIncorrect(deleteUserDto.getMessage())) {
            bindingResult.reject("deleteMessageIncorrect", ERROR_DELETE_MESSAGE_INCORRECT);
            return "user_delete_confirm";
        } else if (userService.isRegisteredForLessThan24(siteUser)) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, ERROR_ACCOUNT_DELETION_BEFORE_24H);
            return "redirect:/user/info";
        } else {
            userService.deleteUser(siteUser);
        }
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, SUCCESS_ACCOUNT_DELETION);
        userService.clearSessionAndCookies(httpServletRequest, httpServletResponse);
        return "redirect:/article/list";
    }
}