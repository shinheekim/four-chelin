package com.example.fourchelin.domain.store.repository;

import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import com.example.fourchelin.domain.store.repository.support.StoreRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    @Query("""
        SELECT s
        FROM Store s
        WHERE (:star IS NULL OR s.star = :star)
          AND (:status IS NULL OR s.status = :status)
        ORDER BY s.updateAt DESC
    """)
    Page<Store> findByFilters(@Param("star") Integer star,
                              @Param("status") StoreStatus status,
                              Pageable pageable);
}
