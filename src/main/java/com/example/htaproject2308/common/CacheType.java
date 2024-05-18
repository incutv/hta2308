package com.example.htaproject2308.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 각 필드에 대한 getter 자동 생성.
@Getter
// 모든 final 필드와 nonnull 필드를 파라미터로 받는 생성자 자동생성.
@RequiredArgsConstructor
public enum CacheType {

    // 캐시 이름 / 만료 시간 / 캐시 최대 크기
    NOTICE_TOP10VIEWS("NoticeMapper.getTop10Views", 30, 15);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

}
