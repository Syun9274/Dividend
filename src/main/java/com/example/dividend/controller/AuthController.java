package com.example.dividend.controller;

import com.example.dividend.model.request.Auth.SignIn;
import com.example.dividend.model.request.Auth.SignUp;
import com.example.dividend.security.TokenProvider;
import com.example.dividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestBody SignUp request) {
        var result = memberService.registerMember(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody SignIn request) {
        var user = memberService.authenticate(request);
        var token = tokenProvider.generateToken(user.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }
}
