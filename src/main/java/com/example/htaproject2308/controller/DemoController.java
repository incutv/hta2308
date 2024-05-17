package com.example.htaproject2308.controller;

import com.example.htaproject2308.dto.MailVO;
import com.example.htaproject2308.dto.Notice;
import com.example.htaproject2308.service.NoticeService;
import com.example.htaproject2308.task.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private final NoticeService noticeService;
    private SendMail sendMail;

    public DemoController(NoticeService noticeService, SendMail sendMail){
        this.noticeService=noticeService;
        this.sendMail=sendMail;
    }

    @GetMapping("")
    public ResponseEntity<Object> findAll() {
        List<Notice> notices = noticeService.getAllNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/top10Views")
    public ResponseEntity<Object> findTop10Views() {
        long beforeTime = System.currentTimeMillis();
        List<Notice> notices = noticeService.getTop10Views();
        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        logger.info("TOP 10 - 실행 시간(ms): " + diffTime);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/mail")
    public Map<String, String> mailSend() {
        Map<String, String> test = new HashMap<>();

        // 원하는 스레드 풀 크기 설정
        int processors = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.max(2, processors); // 최소한 2개의 스레드는 사용
        ExecutorService customThreadPool = Executors.newWorkStealingPool(threadPoolSize);

        List<Notice> notices = noticeService.getOneNotice();
        MailVO vo = new MailVO();
        vo.setContent(notices.get(0).getContent());

        long beforeTime = System.currentTimeMillis();

        /* 동기 방식 */
        notices.forEach(notice ->
                       sendMail.sendMail(vo, "동기")
        );
        test.put("테스트", "동기");

        /* 비동기 방식 */
//        notices.forEach(notice ->
//                CompletableFuture.runAsync(() -> sendMail.sendMail(vo, "비동기"), customThreadPool)
//                        .exceptionally(throwable -> {
//                            logger.error("Exception occurred: " + throwable.getMessage());
//                            // 이슈 발생을 담당자가 인지 할수 있도록 추가적인 코드가 필요
//                            //test.put("테스트", "실패");
//                            return null;
//                        })
//        );
//        test.put("테스트", "비동기");

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        logger.info("메일 테스트 - 실행 시간(ms): " + diffTime);

        return test;
    }
}


