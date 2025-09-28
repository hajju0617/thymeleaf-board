package com.project.thymeleafboard.common;

public class GlobalConst {

    public static final int SUCCESS = 1;
    public static final int DELETE = -1;
    public static final int FAIL = 0;

    public static final String FROM_EMAIL = "hajju0617@naver.com";
    public static final String SERVER_ERROR = "죄송합니다. 잠시 후 다시 시도해 주세요.";
    public static final String TITLE_REQUIRED = "제목을 입력해 주세요.";
    public static final String CONTENT_REQUIRED = "내용을 입력해 주세요.";
    public static final String USERNAME_REQUIRED = "아이디를 입력해 주세요. (영문 소문자와 숫자를 사용해서 5~15자리)";
    public static final String PW_REQUIRED = "비밀번호를 입력해 주세요. (최소 1개 이상의 영문 소문자와 숫자, 특수문자(!@#$%^&*)를 조합해서 8~20자리)";
    public static final String EMAIL_REQUIRED = "올바른 이메일을 입력해 주세요.";
    public static final String EMAIL_ALREADY_REGISTERED = "이미 가입되어 있는 이메일이에요.";
    public static final String EMAIL_NOT_FOUND = "해당 이메일로 가입된 아이디가 없어요. 이메일 주소를 다시 확인해 주세요.";
    public static final String USERNAME_ALREADY_REGISTERED = "이미 가입되어 있는 아이디예요.";
    public static final String PASSWORD_INCORRECT = "비밀번호가 서로 일치하지 않아요.";
    public static final String VERIFICATION_CODE_SENT = "입력하신 이메일로 인증번호가 전송 되었어요.";
    public static final String EMAIL_VERIFICATION_SUCCESS = "인증이 성공적으로 완료되었어요.";
    public static final String EMAIL_VERIFICATION_FAILED = "인증 번호가 올바르지 않거나 만료되었어요.";
    public static final String USER_NOT_FOUND_BY_USERNAME_AND_EMAIL = "입력하신 아이디와 이메일로 가입된 회원을 찾을 수 없어요.";
    public static final String SUCCESS_SIGNUP = "회원가입이 성공적으로 완료되었어요!";
    public static final String SUCCESS_TEMP_PASSWORD_SENT_EMAIL = "임시 비밀번호가 이메일로 발송되었어요.";
}
