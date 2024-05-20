package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class EmailSendService {
    private final JavaMailSender sender;

    public void send(EmailDto emailDto){
        log.info("emailDto : "+emailDto.getSubject());
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailDto.getTo());
        msg.setSubject(emailDto.getSubject());
        msg.setText(emailDto.getText());
        sender.send(msg);
    }
}
