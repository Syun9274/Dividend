package com.example.dividend.exception.custom;

import com.example.dividend.exception.BasicException;
import org.springframework.http.HttpStatus;

public class MemberException {

    public static class AlreadyExistUserException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "이미 존재하는 사용자명 입니다.";
        }
    }

    public static class NotExistUserException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "사용자가 존재하지 않습니다.";
        }
    }

    public static class WrongPassword extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "비밀번호가 올바르지 않습니다.";
        }
    }
}
