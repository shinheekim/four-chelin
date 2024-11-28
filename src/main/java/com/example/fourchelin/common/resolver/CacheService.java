package com.example.fourchelin.common.resolver;

import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class CacheService {

    @Qualifier("")
    private final CacheManager defaultCacheManager;

    public void displayCache(String cacheName) {
        Cache cache = defaultCacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("cacheName = " + cacheName);
            return;
        }

        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof ConcurrentMap<?, ?> concurrentMap) {
            System.out.println("Cached data:");
            concurrentMap.forEach((key, value) ->
                    System.out.println("Key: " + key + ", Value: " + value)
            );
        }

    }

    public Optional<Long> getCacheData(String cacheName, String sessionId) {

        Cache cache = defaultCacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("cacheName = " + cacheName);
            return null;
        }

        return Optional.ofNullable(cache.get(sessionId, LoginResponse.class).id());

    }

}
