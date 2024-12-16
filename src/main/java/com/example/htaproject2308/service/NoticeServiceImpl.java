package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Cacheable(value = "NoticeReadMapper.findTop10Views" , condition = "#top10Views != null and #top10Views.size() > 0")
    public List<Notice> getTop10Views() {

        List<Notice> allNotices = noticeReadMapper.findAll();

        List<Notice> top10Views = allNotices.stream()
                .sorted(Comparator.comparingInt(Notice::getViews).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return top10Views;
    }




}





