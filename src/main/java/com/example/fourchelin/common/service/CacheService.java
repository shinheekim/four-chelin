package com.example.fourchelin.common.service;

import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public void displayCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("cacheName = " + cacheName);
            return;
        }

        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof java.util.concurrent.ConcurrentMap<?, ?> concurrentMap) {
            System.out.println("Cached data:");
            concurrentMap.forEach((key, value) ->
                    System.out.println("Key: " + key + ", Value: " + value)
            );
        }

    }

    public Optional<Long> getCacheData(String cacheName, String sessionId) {

        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("cacheName = " + cacheName);
            return null;
        }

        return Optional.ofNullable(cache.get(sessionId, LoginResponse.class).id());

    }
    // 캐시에 검색 키워드 횟수 저장 및 카운팅
    @CachePut(value = "keywordCounts", key = "#keyword")
    public Long saveOrUpdateKeywordCount(String keyword) {
        Cache cache = cacheManager.getCache("keywordCounts");
        Long currentCount = (cache != null) ? cache.get(keyword, Long.class) : null;
        return (currentCount != null) ? currentCount + 1 : 1L;
    }
    // 캐시1 데이터 DB에 저장
    public Map<String, Long> getAllKeywordCountsFromCache1() {
        Cache cache = cacheManager.getCache("keywordCounts");
        if (cache == null) {
            return Map.of();
        }
        Map<String, Long> allKeywordCounts = new HashMap<>();
        ConcurrentMap<Object, Object> nativeCache = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        nativeCache.forEach((key, value) -> {
            if (key instanceof String && value instanceof Long) {
                allKeywordCounts.put((String) key, (Long) value);
            }
        });
        return allKeywordCounts;
    }
    // 캐시2 데이터 조회
    public Map<String, Long> getAllKeywordCountsFromCache2() {
        Cache cache = cacheManager.getCache("keywordCounts");
        if (cache == null) {
            return Map.of();
        }
        Map<String, Long> allKeywordCounts = new HashMap<>();
        ConcurrentMap<Object, Object> nativeCache = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        nativeCache.forEach((key, value) -> {
            if (key instanceof String && value instanceof Long) {
                allKeywordCounts.put((String) key, (Long) value);
            }
        });
        return allKeywordCounts;
    }
    // 캐시 초기화
    public void clear(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
