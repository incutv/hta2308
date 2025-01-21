package com.example.htaproject2308.controller;

import com.example.htaproject2308.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(path = "/stream")
    public SseEmitter streamNotifications() {
        Long userId = 12345L;
        // 사용자의 정보를 받아 연결을 맺음
        return notificationService.createEmitter(userId);
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendNotification() {
        Long receiverId = 12345L;
        notificationService.sendNotification(receiverId);
        return ResponseEntity.ok("Notification sent successfully");
    }
}
