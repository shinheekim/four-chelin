package com.example.fourchelin.domain.waiting.repository;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMemberIdAndStoreIdAndStatus(Long memberId, Long storeId, WaitingStatus waitingStatus);
    Long countByStoreIdAndStatus(Long storeId, WaitingStatus status);
    List<Waiting> findAllByMemberIdAndStatus(Long memberId, WaitingStatus waitingStatus);
    List<Waiting> findAllByStoreIdAndStatus(Long storeId, WaitingStatus waitingStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Waiting> findByIdAndStatus(Long id, WaitingStatus status);

    @Query("SELECT w FROM Waiting w WHERE w.store.id = :storeId AND w.status = :status")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Waiting> findAllWithExclusiveLock(@Param("storeId") Long storeId, @Param("status") WaitingStatus status);
}
