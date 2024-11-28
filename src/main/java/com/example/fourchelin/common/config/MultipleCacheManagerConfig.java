package com.example.fourchelin.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@EnableCaching
@Configuration
public class MultipleCacheManagerConfig extends CachingConfigurerSupport {

    public static final String WAITING_VIEW_COUNT_PREFIX = "waiting:view:";

    // Redis Cache Manager
    @Bean
    public CacheManager cacheManager() {

        CompositeCacheManager cacheManager = new CompositeCacheManager();
        List<CacheManager> managers = new ArrayList<CacheManager>();
        managers.add(new ConcurrentMapCacheManager());

    }


}
