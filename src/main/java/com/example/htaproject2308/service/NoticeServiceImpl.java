package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeReadMapper noticeReadMapper;

    @Autowired
    public NoticeServiceImpl(NoticeReadMapper noticeReadMapper) {
        this.noticeReadMapper = noticeReadMapper;
    }

    @Override
    public List<Notice> getAllNotices() {
        return noticeReadMapper.findAll();
    }

    @Override
    // 캐시의 이름 / 캐시적용 조건(빈문자열은 항상 캐시 사용)
    @Cacheable(value = "NoticeMapper.getTop10Views")
    public List<Notice> getTop10Views() {
        log.info("Fetching top 10 viewed notices from database");
        List<Notice> notices = noticeReadMapper.findTop10Views();
        // 정렬처리
        notices.sort(Comparator.comparingInt(Notice::getViews)
                .thenComparing(Notice::getCreateDate)
                .reversed());
        log.info("Notices fetched: {}", notices);
        System.out.println("why");
        return notices;
    }
}





