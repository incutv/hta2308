package com.example.htaproject2308.config;

import com.example.htaproject2308.common.CacheType;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Autowired
    private NoticeReadMapper noticeReadMapper;

    /*@Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
                        .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.SECONDS)
                        .maximumSize(cache.getMaximumSize())
                        .build()))
                .toList();
    }*/

    @Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values())
                .map(cache -> {
                    LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                            .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.SECONDS)
                            .maximumSize(cache.getMaximumSize())
                            .refreshAfterWrite(30, TimeUnit.SECONDS) // 캐시 갱신 설정
                            .build(key -> loadCacheData(key));  // 데이터를 로드하는 로직

                    // 캐시를 CaffeineCache로 변환하여 반환
                    return new CaffeineCache(cache.getCacheName(), loadingCache);
                })
                .toList();
    }

    @Bean
    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

    // 캐시 데이터를 로드하는 메서드 (예: DB 조회)
    private Object loadCacheData(Object key) {
        if ("NoticeMapper.findAll".equals(key)) {
            // 실제 DB 호출 또는 다른 데이터를 로드하는 로직을 추가
            return noticeReadMapper.findAll();  // 예시
        }
        return null;
    }
}
