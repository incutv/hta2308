package com.example.htaproject2308.dto;

import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
public class Notification {
    private Long id;
    private Long receiverId; // User ID를 직접 사용
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
