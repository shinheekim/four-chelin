package com.example.fourchelin.domain.member.repository;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.repository.support.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

}
