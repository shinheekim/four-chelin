package com.example.fourchelin.domain.store.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StorePageResponse findStores(String keyword, String category, Integer star, int page, Integer size, String criteria) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc(criteria)));

        Page<Store> stores = storeRepository.findByConditionStores(keyword, category, star, pageable);

        return new StorePageResponse(stores);

    }
    public StorePageResponse searchStore(String keyword, int page, int size, int star, String status) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new StoreException("검색어를 입력해주세요");
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByFilters(keyword, star, status, pageable);

        return new StorePageResponse(stores);

    }
    // Supabase(PostgreSQL) 인덱스 생성

    public void createIndexs(){
        try {
            jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idx_total_star_store_status ON store (total_star, store_status);
            """);

            jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idx_monitoring_date ON store (monitoring_date DESC);
            """);

            System.out.println("Indexs created");
        } catch (Exception e) {
            System.err.println("Error while creating indexes: " + e.getMessage());
            throw new StoreException("인덱스 생성 중 오류가 발생했습니다: " );
        }
}
