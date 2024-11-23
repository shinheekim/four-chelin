package com.example.fourchelin.domain.member.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RspTemplate<SignupResponse>> signup(@RequestBody SignupRequest req) {

        RspTemplate<SignupResponse> res = new RspTemplate<>(
                HttpStatus.CREATED,
                "회원가입에 성공하였습니다.",
                memberService.signup(req)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }


}
