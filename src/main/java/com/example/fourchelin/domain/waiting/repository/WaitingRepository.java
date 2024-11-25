package com.example.fourchelin.domain.waiting.repository;

import com.example.fourchelin.domain.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
