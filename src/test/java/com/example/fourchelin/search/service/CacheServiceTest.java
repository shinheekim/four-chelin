package com.example.fourchelin.search.service;

import com.example.fourchelin.common.service.CacheService;
import com.example.fourchelin.domain.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SearchService searchService;
    @Autowired
    private KeywordCacheService keywordCacheService;

    @Test
    void cachePutAndGetTest() {
        keywordCacheService.saveOrUpdateKeywordCountWithCache("카페", LocalDate.now());
        cacheService.displayCache("keywordCount");
    }
}