package com.example.htaproject2308.task;


import com.example.htaproject2308.dto.MailVO;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class SendMail {

    private JavaMailSenderImpl mailSender;

    @Autowired
    public SendMail(JavaMailSenderImpl mailSender) {
        this.mailSender=mailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

    public void sendMail(MailVO vo, String type) {

        long beforeTime = System.currentTimeMillis();

        MimeMessagePreparator mp = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper
                        = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom(vo.getFrom());
                helper.setTo(vo.getTo());
                helper.setSubject(vo.getSubject());
                helper.setText(vo.getContent(), true);

            }
        };
        mailSender.send(mp);

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        logger.info("메일 " + type + " 테스트  - 실행 시간(ms): " + diffTime);
        logger.info("메일을 전송 완료했습니다.");
    }
}
