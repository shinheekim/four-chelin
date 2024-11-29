package com.example.fourchelin.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(defaultConfiguration())
                .build();
    }

    @Bean
    @Primary
    public CacheManager defaultCacheManager() {
        return new ConcurrentMapCacheManager();
    }


//    @Bean
//    LettuceConnectionFactory connectionFactory() {
//        return new LettuceConnectionFactory();
//    }
//
//
//    @Bean(name = "redisCacheManager")
//    public CacheManager redisCacheManager() {
//
//        return RedisCacheManager.RedisCacheManagerBuilder
//                .fromConnectionFactory(connectionFactory())
//                .cacheDefaults(defaultConfiguration())
//                .withInitialCacheConfigurations(configureMap())
//                .build();
//    }
//
//    private Map<String, RedisCacheConfiguration> configureMap() {
//        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
//        cacheConfigurationMap.put("loginMember", defaultConfiguration());
//        return cacheConfigurationMap;
//
//    }
//
    private RedisCacheConfiguration defaultConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofDays(1));
    }
//
//    @Bean(name = "defaultCache")
//    public CacheManager defaultCacheManager() {
//        return new ConcurrentMapCacheManager("member");
//    }
//
//    public CacheResolver cacheResolver() {
//        return new MultipleCacheResolver(defaultCacheManager(), redisCacheManager());
//    }

//    @Bean
//    public CacheResolver redisCacheResolver() {
//        return new RedisCacheResolver(redisCacheManager());
//    }
//
//    @Bean
//    public CacheResolver defaultCacheResolver() {
//        return new DefaultCacheResolver(defaultCacheManager());
//    }


}
