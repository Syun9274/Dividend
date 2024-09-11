package com.example.dividend.model.request;

import com.example.dividend.model.entity.Member;
import com.example.dividend.model.enums.Authority;
import lombok.Data;

import java.util.List;

public class Auth {

    @Data
    public static class SignIn {

        private String username;
        private String password;
    }

    @Data
    public static class SignUp {

        private String username;
        private String password;

        private List<Authority> roles;

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
