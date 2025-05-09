package com.example.htaproject2308.dto;

public class MailVO {
    private String from = "zilun@naver.com";
    private String to = "a66074742@gmail.com";
    private String subject = "Jhta 멘토링"; // 제목
    private String content; // 내용

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
