package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;

public record SignupResponse(Long id, String phone, String nickname, MemberRole role) {

    public static SignupResponse from(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname(),
                member.getRole()
        );
    }
}
