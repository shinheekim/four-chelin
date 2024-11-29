//package com.example.fourchelin.common.resolver;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.interceptor.CacheOperationInvocationContext;
//import org.springframework.cache.interceptor.CacheResolver;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@RequiredArgsConstructor
//public class MultipleCacheResolver implements CacheResolver {
//
//    private final CacheManager defaultCacheManager;
//    private final CacheManager redisCacheManager;
//    private static final String LOGIN_MEMBER = "loginMember";
//    private static final String MEMBER = "member";
//
//    @Override
//    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
//        Collection<Cache> caches = new ArrayList<Cache>();
//        if ("login".equals(context.getMethod().getName())) {
//            caches.add(redisCacheManager.getCache(LOGIN_MEMBER));
//        } else {
//            caches.add(defaultCacheManager.getCache(MEMBER));
//        }
//        return caches;
//    }
//}
