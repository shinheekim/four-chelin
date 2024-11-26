package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;

public record FindMemberResponse(Long id, String phone, String nickname, MemberRole role) {

    public static FindMemberResponse from(Member member) {
        return new FindMemberResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname(),
                member.getRole()
        );
    }
}
