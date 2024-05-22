package com.example.htaproject2308.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String to;  // 받는사람
    private String subject;  // 제목
    private String text;     // 내용
}
