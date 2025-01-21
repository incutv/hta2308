package com.example.htaproject2308.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationViewController {
    @GetMapping("/notifications")
    public String notificationsPage() {
        return "notifications"; // templates/notifications.html로 매핑됨
    }
}
