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
    @Cacheable(value = "NoticeServiceImpl.getAllNotices")
    public List<Notice> getAllNotices() {
        log.info("getAllNotices log print");
        return noticeReadMapper.findAll();
    }

    @Override
    @Cacheable(value = "NoticeServiceImpl.getTop10Views")
    public List<Notice> getTop10Views() {
        log.info("getTop10Views log print");
        List<Notice> notices = noticeReadMapper.findTop10Views();
        if (!notices.isEmpty()) {
            Comparator<Notice> comparator = Comparator.comparingInt(Notice::getViews)
                    .thenComparing(Notice::getCreateDate)
                    .reversed();

            notices.sort(comparator);
        }
        return notices;
    }
}





