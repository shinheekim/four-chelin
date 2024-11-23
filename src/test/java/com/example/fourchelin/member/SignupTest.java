package com.example.fourchelin.member;

import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import com.example.fourchelin.domain.member.service.MemberService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SignupTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Test
    @Rollback(value = false)
    @DisplayName("정상적인 회원가입")
    @Disabled
    void signup() {

        // Given
        String phone = "01076121665";
        String nickname = "플레이브사랑해";
        String password = "plave1234";

        SignupRequest req = new SignupRequest(phone, nickname, password);

        MemberService memberService = new MemberService(memberRepository, encoder);

        // When
        SignupResponse res = memberService.signup(req);

        // Then
        assertEquals(phone, res.phone());
        assertEquals(nickname, res.nickname());

    }
}
