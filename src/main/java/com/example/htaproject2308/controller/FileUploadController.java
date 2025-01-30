package com.example.htaproject2308.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class FileUploadController {
    @GetMapping("/")
    public String uploadPage() {
        return "upload"; // templates/upload.html 렌더링
    }
}
