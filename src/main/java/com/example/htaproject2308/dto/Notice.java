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

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public int getViews() {
        return views;
    }

    public String getWho() {
        return who;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

}
