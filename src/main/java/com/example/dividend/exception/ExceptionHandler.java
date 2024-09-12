package com.example.dividend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BasicException.class)
    protected ResponseEntity<?> handleBasicException(BasicException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.statusCode())
                .message(e.errorMessage())
                .build();

        return new ResponseEntity<>(errorResponse, Objects.requireNonNull(HttpStatus.resolve(e.statusCode())));
    }
}
