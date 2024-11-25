package com.example.fourchelin.domain.member.repository.support;

import com.example.fourchelin.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByPhone(String phone);
}
