package com.example.dividend.exception.custom;

import com.example.dividend.exception.BasicException;
import org.springframework.http.HttpStatus;

public class CompanyException {

    public static final class NoCompanyException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "존재하지 않는 회사명 입니다.";
        }
    }

    public static final class AlreadyExistCompanyException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "해당 회사가 이미 목록에 존재합니다.";
        }
    }

    public static class EmptyTickerException extends BasicException {

        @Override
        public int statusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String errorMessage() {
            return "Ticker값이 입력되지 않았습니다. 해당 부분은 비워둘 수 없습니다.";
        }
    }
}
