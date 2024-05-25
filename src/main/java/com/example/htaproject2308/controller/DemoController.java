package com.example.htaproject2308.controller;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.service.NoticeAlarmSendService;
import com.example.htaproject2308.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoController {
    private final NoticeService noticeService;
    private final NoticeAlarmSendService emailSendService;


    @GetMapping("")
    public ResponseEntity<Object> findAll() {
        List<Notice> notices = noticeService.getAllNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/top10Views")
    public ResponseEntity<Object> findTop10Views() {
        List<Notice> notices = noticeService.getTop10Views();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }
// nio-8080-exec-1
    @GetMapping("/email/confirm")
    public ResponseEntity<Object> mailSend() {
        List<Notice> notices = noticeService.getTop10Views();
        if(!notices.isEmpty()){
            emailSendService.batchSend(notices);
        }
        // 회원가입 , 여행찜 ,
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}


