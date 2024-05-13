package com.example.htaproject2308.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private long id;
    private String title;
    private String content;
    private String who;

    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public LocalDateTime getCreDateTime() {
        return createDate;
    }

    }
