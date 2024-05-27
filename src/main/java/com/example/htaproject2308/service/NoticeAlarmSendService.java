package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.EmailDto;
import com.example.htaproject2308.dto.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class NoticeAlarmSendService extends  EmailSendService{
    public static final String TO_EMAIL = "study.eunho@gmail.com";

    public NoticeAlarmSendService(JavaMailSender sender) {
        super(sender);
    }

    int processors = Runtime.getRuntime().availableProcessors();
    int threadPoolSize = Math.max(2, processors);
    ExecutorService customThreadPool = Executors.newFixedThreadPool(10);


    public void batchSend(List<Notice> notices) {
        notices.forEach(notice ->
                CompletableFuture.runAsync(() -> send(setNoticeAlarmContent(notice)))
                        .exceptionally(throwable -> {
                            log.error("Exception occurred: " + throwable.getMessage());
                            return null;
                        })
        );
    }

    public EmailDto setNoticeAlarmContent(Notice notice) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(TO_EMAIL);
        emailDto.setText(notice.getContent());
        emailDto.setSubject(notice.getTitle());
        return emailDto;
    }
}
