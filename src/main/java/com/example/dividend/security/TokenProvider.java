package com.example.dividend.security;

import com.example.dividend.exception.custom.SecurityException.EmptyKeyException;
import com.example.dividend.model.enums.Authority;
import com.example.dividend.service.MemberService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    private final MemberService memberService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private byte[] decodedKey;

    @PostConstruct
    public void init() {
        if (secretKey != null) {
            decodedKey = Base64.getDecoder().decode(secretKey);
            log.info("Decoded Secret Key initialized successfully.");
        } else {
            log.error("Secret Key cannot be null");
            throw new EmptyKeyException();
        }
    }

    public String generateToken(String username, List<Authority> roles) {
        Claims claims = Jwts.claims().setSubject(username);

        List<String> roleNames = roles.stream()
                .map(Authority::name)
                .collect(Collectors.toList());
        claims.put(KEY_ROLES, roleNames);

        var now = new Date();
        var expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        log.info("Generating token for user: {}", username);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, decodedKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        log.info("Authenticating token for user: {}", getUsername(token));
        UserDetails userDetails = memberService.loadUserByUsername(getUsername(token));

        List<GrantedAuthority> authorities = getAuthorities(token);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            var claims = parseClaims(token);
            boolean isValid = !claims.getExpiration().before(new Date());
            log.info("Token validation result: {}", isValid);
            return isValid;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            log.info("Parsing claims for token");
            return Jwts.parser()
                    .setSigningKey(decodedKey)  // Base64로 디코딩된 key를 사용
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            log.warn("Token expired");
            return e.getClaims();

        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = parseClaims(token);
        List<String> roles = (List<String>) claims.get(KEY_ROLES);
        log.info("Extracted roles from token: {}", roles);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
