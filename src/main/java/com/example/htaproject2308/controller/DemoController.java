package com.example.htaproject2308.controller;

import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.service.NoticeAlarmSendService;
import com.example.htaproject2308.service.NoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final NoticeService noticeService;
    private final NoticeAlarmSendService emailSendService;




    public DemoController(NoticeService noticeService, NoticeAlarmSendService emailSendService) {
        this.noticeService=noticeService;
        this.emailSendService=emailSendService;
    }

    @GetMapping("")
    public ResponseEntity<Object> findAll() {
        List<Notice> notices = noticeService.getAllNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/top10Views")
    public ResponseEntity<Object> findTop10Views() {
        //POSTMAN으로 views 상위10개 데이터 조회 테스트 확인 완료
        List<Notice> notices = noticeService.getTop10Views();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }


    @GetMapping("/email")
    public ResponseEntity<Object> mailSend() {
        List<Notice> notices = noticeService.getTop10Views();
        if(!notices.isEmpty()){
            emailSendService.batchSend(notices);
        }
        // 회원가입 , 여행찜 ,
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}


