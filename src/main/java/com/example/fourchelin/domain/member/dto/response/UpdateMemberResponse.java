package com.example.fourchelin.domain.member.dto.response;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;

public record UpdateMemberResponse(Long id, String phone, String nickname, MemberRole role) {

    public static UpdateMemberResponse from(Member member) {
        return new UpdateMemberResponse(
                member.getId(),
                member.getPhone(),
                member.getNickname(),
                member.getRole()
        );
    }
}
