package com.example.htaproject2308.service;


import com.example.htaproject2308.dto.Email;
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

    public void send(Email email){
        log.info("emailDto : "+email.getSubject());
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email.getTo());
        msg.setSubject(email.getSubject());
        msg.setText(email.getText());
        sender.send(msg);
    }
}
