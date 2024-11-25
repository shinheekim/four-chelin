package com.example.fourchelin.domain.member.service;

import com.example.fourchelin.domain.member.dto.request.LoginRequest;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.member.exception.MemberException;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest req) {

        String phone = req.phone();
        String nickname = req.nickname();
        String password = passwordEncoder.encode(req.password());
        MemberRole role = req.role();

        Member createMember = Member.createMember(phone, nickname, password, role);

        memberRepository.save(createMember);

        return SignupResponse.from(createMember);

    }

    public LoginResponse login(HttpSession session, LoginRequest req) {
        String phone = req.phone();
        String rawPassword = req.password();

        Member member = memberRepository.findByPhone(phone).orElseThrow(() ->
                new MemberException("회원정보가 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new MemberException("패스워드가 일치하지 않습니다.");
        }

        session.setAttribute("LOGIN_MEMBER", member);

        return LoginResponse.from(member);

    }
}
