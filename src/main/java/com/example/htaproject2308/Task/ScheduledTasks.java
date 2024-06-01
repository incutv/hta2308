package com.example.htaproject2308.Task;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.service.EmailService;
import com.example.htaproject2308.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    private EmailService emailService;

    @Autowired
    private NoticeService noticeService;

    @Scheduled(cron = "0 0 18 * * ?")
    public void sendHotPostsEmail() {

        // HOT 게시글 10개를 가져옵니다.
        List<Notice> hot10 = noticeService.getTop10Views();

        // 이메일 내용 생성
        StringBuilder emailContent = new StringBuilder("Today's HOT Posts:\n\n");
        for (Notice notice : hot10) {
            emailContent.append(notice.getTitle()).append("\n")
                    .append(notice).append("\n\n");
        }

        // 이메일 전송
        emailService.sendSimpleMessage("rlatks15@naver.com","TODAY TOP 10",emailContent.toString());
    }
}
