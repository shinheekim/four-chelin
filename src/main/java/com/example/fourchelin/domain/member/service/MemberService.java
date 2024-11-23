package com.example.fourchelin.domain.member.service;

import com.example.fourchelin.domain.member.dto.request.LoginRequest;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.exception.MemberException;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest req) {

        String phone = req.phone();
        String nickname = req.nickname();
        String password = passwordEncoder.encode(req.password());

        Member member = Member.of(phone, nickname, password);
        memberRepository.save(member);

        return SignupResponse.from(member);

    }

    public LoginResponse login(LoginRequest req) {
        String phone = req.phone();
        String rawPassword = req.password();

        Member member = memberRepository.findByPhone(phone).orElseThrow(() ->
                new MemberException("Not Found User")
        );

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new MemberException("The Password does not match");
        }

        return LoginResponse.from(member);

    }
}
