package com.project.thymeleafboard.common;

public class GlobalConst {
    private GlobalConst() {    }

    // Common   ========================================================================================================
    public static final String SUCCESS_MSG = "successMsg";
    public static final String ERROR_MSG = "errorMsg";
    public static final String SERVER_ERROR = "죄송합니다. 잠시 후 다시 시도해 주세요.";
    public static final String ERROR_QUERY_STRING_MISMATCH = "정상적인 요청이 아니예요. 다시 시도해주세요.";
    public static final String CONTENT_REQUIRED = "내용을 입력해 주세요.";
    public static final String ERROR_NEGATIVE_PAGE_NUMBER = "페이지 번호는 음수가 될 수 없어요.";
    public static final String ERROR_NO_CHANGE_DETECTED = "수정된 사항이 없어요.";
    public static final String ERROR_SELF_VOTE = "본인이 작성한 글/댓글은 추천할 수 없어요.";
    public static final String ERROR_AUTHOR_MISMATCH = "본인이 작성한 글/댓글만 수정하거나 삭제 할 수 있어요";
    public static final String SUCCESS_DELETE = "정상적으로 삭제되었어요.";
    public static final String SUCCESS_MODIFY = "정상적으로 수정되었어요.";

    // Article  ========================================================================================================
    public static final String TITLE_REQUIRED = "제목을 입력해 주세요.";
    public static final String ERROR_INVALID_LIST_SIZE = "목록 사이즈는 '10, 20, 40, 50' 중 하나여야 해요.";
    public static final String ERROR_INVALID_SORT_TYPE = "정렬 기준은 '최신순, 추천순, 조회수순' 중 하나여야 해요.";
    public static final String ERROR_SEARCH_OPTION = "검색 옵션은 '제목+내용, 제목만, 글 작성자, 댓글 내용'중 하나여야 해요.";
    public static final String ERROR_ARTICLE_NOT_FOUND = "요청하신 게시글을 찾을 수 없어요.";
    public static final String ERROR_PAGE_OUT_OF_ARTICLE_RANGE = "게시글이 존재하지 않는 페이지예요.";
    public static final String ERROR_SEARCH_KEYWORD = "검색어를 입력해 주세요.";

    // Comment  ========================================================================================================
    public static int commentPageSize = 5;
    public static String ERROR_PAGE_OUT_OF_COMMENT_RANGE = "댓글이 존재하지 않는 페이지예요.";
    public static String ERROR_COMMENT_NOT_FOUND = "요청하신 댓글을 찾을 수 없어요.";
    public static final String ERROR_INVALID_CMT_SORT_TYPE = "정렬 기준은 '최신순, 추천순' 중 하나여야 해요.";

    // User     ========================================================================================================
    public static final String USERNAME_REQUIRED = "아이디를 입력해 주세요. (영문 소문자와 숫자를 사용해서 5~15자리)";
    public static final String PW_REQUIRED = "비밀번호를 입력해 주세요. (최소 1개 이상의 영문 소문자와 숫자, 특수문자(!@#$%^&*)를 조합해서 8~20자리)";
    public static final String EMAIL_REQUIRED = "올바른 이메일을 입력해 주세요.";
    public static final String EMAIL_ALREADY_REGISTERED = "이미 가입되어 있는 이메일이에요.";
    public static final String USERNAME_ALREADY_REGISTERED = "이미 가입되어 있는 아이디예요.";
    public static final String SUCCESS_VERIFICATION_CODE_SENT = "입력하신 이메일로 인증번호가 전송 되었어요.";
    public static final String SUCCESS_SIGNUP = "회원가입이 성공적으로 완료되었어요!";
    public static final String EMAIL_NOT_FOUND = "해당 이메일로 가입되어 있는 계정이 없어요. 이메일 주소를 다시 확인해 주세요.";
    public static final String PASSWORD_INCORRECT = "비밀번호가 서로 일치하지 않아요.";
    public static final String ERROR_USER_NOT_FOUND_BY_USERNAME = "입력하신 아이디로 가입되어 있는 사용자를 찾을 수 없어요.";
    public static final String ERROR_USER_NOT_FOUND_BY_USERNAME_AND_EMAIL = "입력하신 아이디와 이메일로 가입된 회원을 찾을 수 없어요.";
    public static final String ERROR_ALREADY_LOGGED_IN = "\"%s\" 계정으로 이미 로그인되어 있어요.";

    // Email    ========================================================================================================
    public static final String FROM_EMAIL = "hajju0617@naver.com";
    public static final String SUCCESS_EMAIL_VERIFICATION = "인증이 성공적으로 완료되었어요.";
    public static final String SUCCESS_TEMP_PASSWORD_SENT_EMAIL = "임시 비밀번호가 이메일로 발송되었어요.";
    public static final String ERROR_EMAIL_VERIFICATION = "인증 번호가 올바르지 않거나 만료되었어요.";
}
