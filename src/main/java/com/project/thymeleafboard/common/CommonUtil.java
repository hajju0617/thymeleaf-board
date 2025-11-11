package com.project.thymeleafboard.common;

import java.util.Random;

public class CommonUtil {
    private CommonUtil() {  }
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL_CHARS = LOWER + DIGITS + SPECIAL;
    private static final Random random = new Random();

    /*
        아이디 일부분을 '*'로 가리는 메서드.
    */
    public static String maskUsername(String username) {
        int length = username.length();
        int maskCount;
        if (length <= 6) {           // 5~6자리
            maskCount = 2;
        } else if (length <= 9) {    // 7~9자리
            maskCount = 3;
        } else {                     // 10~15자리
            maskCount = 4;
        }

        StringBuilder maskedId = new StringBuilder(username);
        for (int i = length - maskCount; i < length; i++) {
            maskedId.setCharAt(i, '*');
        }
        return maskedId.toString();
    }

    /*
        랜덤 숫자. (인증 번호 6자리, 소셜로그인시 접미사 8자리)
    */
    public static String makeRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }


    /*
        12 자리 랜덤 문자열. (비밀번호)
    */
    public static String makeRandomPassword() {
        int length = 12;
        StringBuilder sb = new StringBuilder(length);
        sb.append(LOWER.charAt(random.nextInt(LOWER.length())));
        sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        sb.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        for (int i = 3; i < length; i++) {
            sb.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        return sb.toString();
    }
}
