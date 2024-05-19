package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.mapper.NoticeReadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

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
    public List<Notice> getTop10Views() {
        // 모든 공지사항을 가져옵니다.
        List<Notice> allNotices = noticeReadMapper.findAll();
        // 조회수를 기준으로 내림차순으로 정렬합니다.
        allNotices.sort((n1, n2) -> {
            if (n1.getViews() != n2.getViews()) {
                return n2.getViews() - n1.getViews(); // 조회수가 다른 경우 내림차순 정렬
            } else {
                // 조회수가 동일한 경우 최근에 생성된 게시글 순으로 정렬
                return n2.getCreateDate().compareTo(n1.getCreateDate());
            }
        });
        // 조회수가 높은 HOT 게시글 10개를 선택합니다.
        int topN = Math.min(10, allNotices.size()); //게시물 수가 10개 이상이면 10, 아니면 게시물 수 만큼
        return allNotices.subList(0, topN);

    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void runAfterTenSecondsRepeatTenSeconds() {
        log.info("10초 후 실행 => time : " + LocalTime.now());
    }
}





