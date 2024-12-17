package com.example.htaproject2308.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    NOTICE_FINDALL("NoticeMapper.findAll", 10, 10000),
    NOTICE_FINDTOP10VIEWS("NoticeMapper.findTop10Views", 10, 1000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
