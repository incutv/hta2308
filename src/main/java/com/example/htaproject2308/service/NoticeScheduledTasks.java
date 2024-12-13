package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class NoticeScheduledTasks {
    private final NoticeReadMapper noticeReadMapper;

    @Autowired
    public NoticeScheduledTasks(NoticeReadMapper noticeReadMapper) {
        this.noticeReadMapper = noticeReadMapper;
    }

    // 1분마다 실행
    @Scheduled(cron = "0 * * * * ?")
    public void dailyNotice() {
        List<Notice> notices = noticeReadMapper.findAll();
        System.out.println("크론 표현식을 사용해 특정 시간에 실행되는 작업");
    }
}
