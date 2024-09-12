package com.example.dividend.exception.custom;

import com.example.dividend.exception.BasicException;
import org.springframework.http.HttpStatus;

public class SecurityException {

    public static final class EmptyKeyException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.UNAUTHORIZED.value();
        }

        @Override
        public String errorMessage() {
            return "키 값이 존재하지 않습니다.";
        }
    }
}
