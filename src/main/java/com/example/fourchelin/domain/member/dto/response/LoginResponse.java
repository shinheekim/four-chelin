package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;

public record LoginResponse(Long id, String phone, String nickname, MemberRole role) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname(),
                member.getRole()
        );
    }
}
