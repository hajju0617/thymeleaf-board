package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.dto.FindIdDto;
import com.project.thymeleafboard.dto.UserDto;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

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
        BindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체. (뷰템플릿에서 검증결과 에러를 출력)
    */
    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            // bindingResult.rejectValue("필드명", "에러 코드", "에러 메시지")
            bindingResult.rejectValue("passwordConfirm", "passwordIncorrect", "비밀번호가 서로 일치하지 않아요.");
            return "signup_form";
        }
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DuplicateUsername", "이미 가입되어 있는 아이디예요.");
            return "signup_form";
        }
        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DuplicateEmail", "이미 가입되어 있는 이메일이에요.");
            return "signup_form";
        }
        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            bindingResult.reject("signupError", e.getMessage());
            return "signup_form";
        }
        return "redirect:/article/list";
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
    @GetMapping("/findId")
    public String findId(FindIdDto findIdDto) {
        return "find_id";
    }

    /*
        아이디 찾기 처리 메서드.

        @Param
        findIdDto : 아이디 찾으려고 요청한 이메일 데이터.
        bindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체. (뷰템플릿에서 검증결과 에러를 출력)
        model : 뷰템플릿에서 모델객체 활용.
    */
    @PostMapping("/findId")
    public String findId(@Valid FindIdDto findIdDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "find_id";
        }
        Optional<SiteUser> optionalSiteUser = userService.findByEmail(findIdDto.getEmail());
        if (optionalSiteUser.isEmpty()) {
            bindingResult.rejectValue("email", "emailNotFound", "해당 이메일로 가입된 아이디가 없어요. 이메일 주소를 다시 확인해 주세요.");
        } else {
            String maskedUsername = CommonUtil.maskUsername(optionalSiteUser.get().getUsername());
            model.addAttribute("username", maskedUsername);
        }
        return "find_id";
    }
}
