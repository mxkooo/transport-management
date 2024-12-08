package com.mxkoo.transport_management.Email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String title, String body){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(title);
        mailMessage.setText(body);
        mailSender.send(mailMessage);
    }

}
