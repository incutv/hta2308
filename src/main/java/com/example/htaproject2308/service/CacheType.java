package com.example.htaproject2308.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    NOTICE_FINDALL("NoticeServiceImpl.getAllNotices", 20, 10000),
    NOTICE_TOP_FIND("NoticeServiceImpl.getTop10Views", 20, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
