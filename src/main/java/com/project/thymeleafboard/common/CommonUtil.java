package com.project.thymeleafboard.common;

public class CommonUtil {
    private CommonUtil() {
    }

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
}
