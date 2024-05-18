package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeReadMapper noticeReadMapper;
    private final EmailService emailService; // 필드 추가

    @Autowired
    public NoticeServiceImpl(NoticeReadMapper noticeReadMapper, EmailService emailService) { // EmailService 생성자 주입 추가
        this.noticeReadMapper = noticeReadMapper;
        this.emailService = emailService; // EmailService를 생성자에서 주입받음
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

        // 이메일 발송
        sendEmailWithNotices(notices);

        return notices;
    }

    @Async
    public CompletableFuture<Void> sendEmailWithNotices(List<Notice> notices) {
        String toEmail = "iopz8811@gmail.com";
        String subject = "Top 10 Viewed Notices";

        // 각 공지사항의 content만 추출하여 이메일 내용에 포함
        StringBuilder contentBuilder = new StringBuilder("Top 10 Viewed Notices Contents:\n\n");

        for (Notice notice : notices) {
            contentBuilder.append(notice.getContent()).append("\n\n");
        }

        String content = contentBuilder.toString();


        return emailService.sendEmail(toEmail, subject, content)
                .exceptionally(ex -> {
                    log.error("Failed to send email with top 10 viewed notices", ex);
                    return null;
                });
    }
}





