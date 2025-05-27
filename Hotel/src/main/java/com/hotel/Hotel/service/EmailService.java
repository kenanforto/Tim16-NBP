package com.hotel.Hotel.service;


import com.hotel.Hotel.common.dto.MailBody;
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
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("kforto1@etf.unsa.ba");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.message());

        javaMailSender.send(message);
    }
}
