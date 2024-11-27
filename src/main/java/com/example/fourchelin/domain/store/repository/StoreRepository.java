package com.example.fourchelin.domain.store.repository;

import com.example.fourchelin.domain.store.entity.Store;
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
        WHERE (:keyword IS NULL OR s.storeName LIKE %:keyword%)
          AND (:star IS NULL OR s.star = :star)
          AND (:status IS NULL OR s.status = :status)
        ORDER BY s.monitoringDate DESC
    """)
    Page<Store> findByFilters(@Param("keyword") String keyword,
                              @Param("star") int star,
                              @Param("status") String status,
                              Pageable pageable);
}
