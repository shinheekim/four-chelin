package com.example.fourchelin.common.service;

import com.example.fourchelin.domain.member.dto.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisCacheService {

    private final CacheManager cacheManager;

    @Autowired
    public RedisCacheService(@Qualifier("redisCacheManager") CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Optional<Long> getCacheData(String cacheName, String sessionId) {

        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("cacheName = " + cacheName);
            return null;
        }

        return Optional.ofNullable(cache.get(sessionId, LoginResponse.class).id());

    }

}
