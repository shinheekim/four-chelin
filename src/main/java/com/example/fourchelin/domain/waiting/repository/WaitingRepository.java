package com.example.fourchelin.domain.waiting.repository;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMemberIdAndStoreIdAndStatus(Long memberId, Long storeId, WaitingStatus waitingStatus);
    Long countByStoreIdAndStatus(Long storeId, WaitingStatus status);
    List<Waiting> findAllByMemberIdAndStatus(Long memberId, WaitingStatus waitingStatus);
    List<Waiting> findAllByStoreIdAndStatus(Long storeId, WaitingStatus waitingStatus);
}
