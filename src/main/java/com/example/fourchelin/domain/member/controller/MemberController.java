package com.example.fourchelin.domain.member.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.member.dto.request.LoginRequest;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public RspTemplate<SignupResponse> signup(@RequestBody @Valid SignupRequest req) {

        SignupResponse res = memberService.signup(req);
        return new RspTemplate<>(HttpStatus.CREATED, "회원가입에 성공하였습니다.", res);
    }

    @PostMapping("/login")
    public RspTemplate<LoginResponse> login(HttpSession session, @RequestBody @Valid LoginRequest req) {

        LoginResponse res = memberService.login(session, req);

        return new RspTemplate<>(HttpStatus.OK, "로그인에 성공하였습니다.", res);

    }
}
