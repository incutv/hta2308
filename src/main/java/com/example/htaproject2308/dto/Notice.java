package com.example.htaproject2308.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice implements Comparable<Notice> {
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

    @Override
    public int compareTo(Notice o) {
        if(this.views>o.views) return 1;
        else if(this.views==o.views) return this.createDate.compareTo(o.createDate);
        else return -1;
    }
}
