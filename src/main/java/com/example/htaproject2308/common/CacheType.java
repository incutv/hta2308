package com.example.htaproject2308.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheType {
    NOTICE_FINDALL("NoticeReadMapper.findAll", 10, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
