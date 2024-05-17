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
    //@Cacheable(value = "NoticeMapper.findAll", condition = "")
    public List<Notice> getAllNotices() {
        return noticeReadMapper.findAll();
    }

    @Override
    @Cacheable(value = "NoticeMapper.findTop10Views", condition = "")
    public List<Notice> getTop10Views() {
        List<Notice> list = noticeReadMapper.findTop10Views();
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    @Override
    public List<Notice> getOneNotice() {
        return noticeReadMapper.findOne();
    }
}





