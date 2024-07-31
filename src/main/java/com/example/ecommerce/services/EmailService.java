package com.example.ecommerce.services;

import com.example.ecommerce.utils.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailBody.to());
        simpleMailMessage.setSubject(mailBody.subject());
        simpleMailMessage.setText(mailBody.text());
        simpleMailMessage.setFrom("alenjacob.vinu@gmail.com");
        javaMailSender.send(simpleMailMessage);

    }
}
