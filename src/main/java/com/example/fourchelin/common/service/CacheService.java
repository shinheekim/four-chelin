package com.example.fourchelin.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

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
}
