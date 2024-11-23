package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;

public record SignupResponse(Long id, String phone, String nickname) {

    public static SignupResponse from(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname()
        );
    }
}
