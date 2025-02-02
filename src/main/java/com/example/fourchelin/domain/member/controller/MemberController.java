package com.example.fourchelin.domain.member.controller;

import com.example.fourchelin.common.security.UserDetailsImpl;
import com.example.fourchelin.common.service.CacheService;
import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.member.dto.request.DeleteMemberRequest;
import com.example.fourchelin.domain.member.dto.request.LoginRequest;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.request.UpdateMemberRequest;
import com.example.fourchelin.domain.member.dto.response.FindMemberResponse;
import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.dto.response.UpdateMemberResponse;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CacheService cacheService;

    @PostMapping("/signup")
    public RspTemplate<SignupResponse> signup(@RequestBody @Valid SignupRequest req) {

        SignupResponse res = memberService.signup(req);
        return new RspTemplate<>(HttpStatus.CREATED, "회원가입에 성공하였습니다.", res);
    }

    @PostMapping("/login")
    public RspTemplate<LoginResponse> login(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestBody @Valid LoginRequest req) {

        // 캐시저장소에 저장된 데이터 확인
        cacheService.displayCache("member");

        HttpSession session = httpRequest.getSession(true);
        String sessionId = session.getId();
        httpResponse.setHeader("X-Session-ID", sessionId);
        LoginResponse res = memberService.login(session, req);

        return new RspTemplate<>(HttpStatus.OK, "로그인에 성공하였습니다.", res);

    }

    @PutMapping("")
    public RspTemplate<UpdateMemberResponse> updateMember(@RequestBody @Valid UpdateMemberRequest req,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        UpdateMemberResponse res = memberService.updateMember(req, member);

        return new RspTemplate<>(HttpStatus.OK, "회원정보 수정에 성공하였습니다.", res);

    }

    @DeleteMapping("")
    public RspTemplate<Void> deleteMember(@RequestBody @Valid DeleteMemberRequest req,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        memberService.deleteMember(req, member);

        return new RspTemplate<>(HttpStatus.OK, "회원정보 삭제에 성공하였습니다.");

    }

    @GetMapping("/v1")
    public RspTemplate<FindMemberResponse> findMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        FindMemberResponse res = memberService.findMember(member);

        return new RspTemplate<>(HttpStatus.OK, "로그인한 회원정보를 불러왔습니다.", res);

    }

    @GetMapping("/v2")
    public RspTemplate<FindMemberResponse> findMemberWithCache(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 캐시저장소에 저장된 데이터 확인
        cacheService.displayCache("member");

        Member member = userDetails.getMember();
        FindMemberResponse res = memberService.findMemberWithCache(member);

        return new RspTemplate<>(HttpStatus.OK, "로그인한 회원정보를 불러왔습니다.", res);

    }

}
