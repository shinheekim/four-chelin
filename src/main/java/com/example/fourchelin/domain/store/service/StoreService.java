package com.example.fourchelin.domain.store.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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

    public StorePageResponse searchStore(int page, int size, Integer star, StoreStatus status) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Repository 메서드 호출
        Page<Store> stores = storeRepository.findByFilters(
                star != null ? star : null,
                status != null ? status : null,
                pageable
        );

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

    public void insertDataFromCsv(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) { // 첫번째 행은 헤더이므로 제외
                String[] row = rows.get(i);

                String storeName = row[0];
                String status = row[1];
                String category = row[2];
                String address = row[3];
                Integer star = row[4] != null && !row[4].isEmpty() ? Integer.parseInt(row[4]) : null;

                String sql = """
                            INSERT INTO store(store_name, status, category, address, star)
                            VALUES(?,?,?,?,?);
                        """;

                jdbcTemplate.update(sql, storeName, status, category, address, star);
            }

            System.out.println("CSV 파일 데이터가 성공적으로 데이터베이스에 삽입");
        } catch (IOException | CsvException e){
            throw new StoreException("csv 파일 읽기 중 오류 발생:" + e.getMessage());
        } catch (Exception e){
            throw new StoreException("데이터베이스 입력 중 오류 발생 :" + e.getMessage());
        }
    }
}




