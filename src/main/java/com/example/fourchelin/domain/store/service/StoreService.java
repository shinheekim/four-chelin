package com.example.fourchelin.domain.store.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final JdbcTemplate jdbcTemplate;
    private final StoreRepository storeRepository;

    public StorePageResponse findStores(String keyword, String category, Integer star, int page, Integer size, String criteria) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc(criteria)));

        Page<Store> stores = storeRepository.findByConditionStores(keyword, category, star, pageable);

        return new StorePageResponse(stores);

    }

    public StorePageResponse searchStore(String keyword, int page, int size, int star, StoreStatus status) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new StoreException("검색어를 입력해주세요");
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByFilters(keyword, star, status, pageable);

        return new StorePageResponse(stores);

    }
    //인덱스 생성
    @PostConstruct
    public void initIndexes() {
        createIndexes();
    }


    public void createIndexes() {
        try {
            // 인덱스 존재 여부를 체크하고 없으면 생성
            String checkStarStatusIndex = """
            SELECT COUNT(1) 
            FROM INFORMATION_SCHEMA.STATISTICS 
            WHERE TABLE_SCHEMA = DATABASE() 
              AND TABLE_NAME = 'store' 
              AND INDEX_NAME = 'idx_star_status';
        """;

            Integer starStatusIndexExists = jdbcTemplate.queryForObject(checkStarStatusIndex, Integer.class);
            if (starStatusIndexExists == 0) {
                jdbcTemplate.execute("""
                CREATE INDEX idx_star_status 
                ON store (star, status);
            """);
                System.out.println("Index 'idx_star_status' created.");
            }

            String checkUpdateAtIndex = """
            SELECT COUNT(1) 
            FROM INFORMATION_SCHEMA.STATISTICS 
            WHERE TABLE_SCHEMA = DATABASE() 
              AND TABLE_NAME = 'store' 
              AND INDEX_NAME = 'idx_update_at';
        """;

            Integer updateAtIndexExists = jdbcTemplate.queryForObject(checkUpdateAtIndex, Integer.class);
            if (updateAtIndexExists == 0) {
                jdbcTemplate.execute("""
                CREATE INDEX idx_update_at 
                ON store (update_at DESC);
            """);
                System.out.println("Index 'idx_update_at' created.");
            }

        } catch (Exception e) {
            System.err.println("Error while creating indexes: " + e.getMessage());
            throw new StoreException("MySQL 인덱스 생성 중 오류가 발생했습니다.");
        }
    }
    }




