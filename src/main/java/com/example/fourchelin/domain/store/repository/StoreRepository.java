package com.example.fourchelin.domain.store.repository;

import com.example.fourchelin.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
