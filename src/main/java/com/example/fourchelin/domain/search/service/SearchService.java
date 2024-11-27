package com.example.fourchelin.domain.search.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final JdbcTemplate jdbcTemplate;

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

}
