package com.example.fourchelin.domain.waiting.repository;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMemberAndStatus(Member member, WaitingStatus waitingStatus);
}
