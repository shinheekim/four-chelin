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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final JdbcTemplate jdbcTemplate;
    private final StoreRepository storeRepository;


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

            for (int i = 1; i < rows.size(); i++) { // 첫 번째 행은 헤더이므로 제외
                try {
                    String[] row = rows.get(i);

                    // CSV 데이터 읽기
                    Long id = row[0] != null && !row[0].isEmpty() ? Long.parseLong(row[0]) : null;
                    String address = row[1];
                    String storeName = row[2];
                    String createAt = row[3];
                    String updateAt = row[4];
                    Integer star = row[5] != null && !row[5].isEmpty() ? Integer.parseInt(row[5]) : null;
                    String category = row[6];
                    String status = row[7];

                    // 유효성 검증
                    if (storeName == null || storeName.isEmpty()) {
                        throw new StoreException("store_name 값이 비어 있습니다. 행: " + i);
                    }
                    if (status == null || (!status.equals("OPEN") && !status.equals("CLOSED"))) {
                        throw new StoreException("잘못된 status 값: " + status + " 행: " + i);
                    }

                    // SQL 실행
                    String sql = """
                        INSERT INTO store (id, address, store_name, create_at, update_at, star, category, status)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                    """;
                    jdbcTemplate.update(sql, id, address, storeName, createAt, updateAt, star, category, status);

                } catch (Exception e) {
                    // 특정 행 처리 실패 시 로그 출력
                    System.err.println("행 처리 중 오류 발생 (행 번호: " + i + "): " + e.getMessage());
                }
            }

            System.out.println("CSV 파일 데이터가 성공적으로 데이터베이스에 삽입되었습니다.");

        } catch (IOException | CsvException e) {
            throw new StoreException("CSV 파일 읽기 중 오류 발생");
        }
    }
}




