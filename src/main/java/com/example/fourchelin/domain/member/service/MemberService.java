package com.example.fourchelin.domain.member.service;

import com.example.fourchelin.domain.member.dto.request.DeleteMemberRequest;
import com.example.fourchelin.domain.member.dto.request.LoginRequest;
import com.example.fourchelin.domain.member.dto.request.SignupRequest;
import com.example.fourchelin.domain.member.dto.request.UpdateMemberRequest;
import com.example.fourchelin.domain.member.dto.response.FindMemberResponse;
import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import com.example.fourchelin.domain.member.dto.response.SignupResponse;
import com.example.fourchelin.domain.member.dto.response.UpdateMemberResponse;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.member.exception.MemberException;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

        if (memberRepository.findByPhone(createMember.getPhone()).isPresent()) {
            throw new MemberException("이미 가입한 회원입니다.");
        }

        memberRepository.save(createMember);

        return SignupResponse.from(createMember);

    }

    @CachePut(value = "member", key = "#session.id")
    public LoginResponse login(HttpSession session, LoginRequest req) {

        String phone = req.phone();
        String rawPassword = req.password();

        Member member = memberRepository.findByPhone(phone).orElseThrow(() ->
                new MemberException("회원정보가 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new MemberException("패스워드가 일치하지 않습니다.");
        }

        return LoginResponse.from(member);

    }

    @CacheEvict(value = "member", key = "#member.id", beforeInvocation = false)
    public UpdateMemberResponse updateMember(UpdateMemberRequest req, Member member) {

        String nickname = req.nickname();
        String password = passwordEncoder.encode(req.password());

        Member updateMember = member.updateMember(nickname, password);
        memberRepository.save(updateMember);
        return UpdateMemberResponse.from(updateMember);

    }

    @CacheEvict(value = "member", key = "#member.id", beforeInvocation = false)
    public void deleteMember(DeleteMemberRequest req, Member member) {

        String rawPassword = req.password();

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new MemberException("패스워드가 일치하지 않습니다.");
        }

        memberRepository.deleteById(member.getId());
    }

    @Transactional(readOnly = true)
    public FindMemberResponse findMember(Member member) {

        Member findMember = memberRepository.findByPhone(member.getPhone()).orElseThrow(() ->
                new MemberException("회원정보가 존재하지 않습니다.")
        );

        return FindMemberResponse.from(findMember);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "member", key = "#member.id")
    public FindMemberResponse findMemberWithCache(Member member) {

        Member findMember = memberRepository.findByPhone(member.getPhone()).orElseThrow(() ->
                new MemberException("회원정보가 존재하지 않습니다.")
        );

        return FindMemberResponse.from(findMember);
    }
}
