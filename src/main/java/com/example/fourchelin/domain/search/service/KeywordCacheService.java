package com.example.fourchelin.domain.search.service;

import com.example.fourchelin.domain.search.repository.PopularKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordCacheService {

    private final CacheManager cacheManager;
    private final PopularKeywordRepository popularKeywordRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @CachePut(value = "keywordCounts", key = "#keyword")
    public Long saveOrUpdateKeywordCountWithCache(String keyword, LocalDate today) {
        Cache cache = cacheManager.getCache("keywordCount");
        Long newCount = 1L;

        if (cache != null) {
            Long currentCount = cache.get(keyword, Long.class);
            if (currentCount != null) {
                newCount = currentCount + 1;
            }
        }

        cache.put(keyword, newCount);
        return newCount;
    }
}
