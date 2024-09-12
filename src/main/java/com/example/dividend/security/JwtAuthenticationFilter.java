package com.example.dividend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그: 요청 URL 및 인증 헤더 존재 여부 확인
        log.info("Processing request for URI: {}", request.getRequestURI());
        log.info("Authorization Header: {}", request.getHeader(AUTHORIZATION_HEADER) != null ? "Present" : "Not Present");

        String token = resolveToken(request);

        // 로그: 토큰 존재 여부 확인
        if (token != null) {
            log.info("Token resolved: {}", token);

            // 토큰 유효성 검증 로그
            if (tokenProvider.validateToken(token)) {
                log.info("Token is valid. Authenticating...");
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);

                log.info("Authenticated user [{}] for request URI: {}", tokenProvider.getUsername(token), request.getRequestURI());
            } else {
                log.warn("Invalid or expired token for request URI: {}", request.getRequestURI());
            }
        } else {
            log.warn("No token found for request URI: {}", request.getRequestURI());
        }

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // 로그: Bearer 토큰 확인
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            log.info("Bearer token found");
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        log.warn("No Bearer token in the request header");
        return null;
    }
}
