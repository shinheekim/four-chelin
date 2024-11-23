package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;

public record LoginResponse(Long id, String phone, String nickname) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname()
        );
    }
}
