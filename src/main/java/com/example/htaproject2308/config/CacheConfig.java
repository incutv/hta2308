package com.example.htaproject2308.config;

import com.example.htaproject2308.common.CacheType;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
// 캐싱 활성화
@EnableCaching
public class CacheConfig {


    @Bean
    // CacheType에 정의된 각 캐시 타입에 대해 Caffeine 캐시를 생성하고 리스트로 반환
    public List<CaffeineCache> caffeineCaches() {
        // CacheType에 정의된 모든 값을 스트림으로 변환.
        return Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(),
                        Caffeine.newBuilder().recordStats()
                        .expireAfterWrite(cache.getExpiredAfterWrite(),
                                TimeUnit.SECONDS)
                        .maximumSize(cache.getMaximumSize())
                        .build()))
                .toList();
    }

    @Bean
    // SimpleCacheManager를 생성하고, 앞서 생성한 Caffeine 캐시들을 캐시 매니저에 설정.
    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);

        return cacheManager;
    }
}
