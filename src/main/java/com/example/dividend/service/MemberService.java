package com.example.dividend.service;

import com.example.dividend.exception.custom.MemberException.AlreadyExistUserException;
import com.example.dividend.model.entity.Member;
import com.example.dividend.model.request.Auth;
import com.example.dividend.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.dividend.exception.custom.MemberException.NotExistUserException;
import static com.example.dividend.exception.custom.MemberException.WrongPassword;

@Slf4j
@AllArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(NotExistUserException::new);
    }

    public Member registerMember(Auth.SignUp member) {
        boolean exists = memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member.toEntity());
    }

    public Member authenticate(Auth.SignIn member) {
        var user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(NotExistUserException::new);

        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new WrongPassword();
        }

        return user;
    }
}
