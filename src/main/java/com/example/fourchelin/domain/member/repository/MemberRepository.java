package com.example.fourchelin.domain.member.repository;

import com.example.fourchelin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
