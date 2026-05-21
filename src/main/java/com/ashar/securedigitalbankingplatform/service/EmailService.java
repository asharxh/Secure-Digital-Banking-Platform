package com.ashar.securedigitalbankingplatform.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("no-reply@securebank.com");

            mailSender.send(message);

            log.info("Email sent successfully to {}", to);

        } catch (Exception e) {

            log.error("Email sending failed to {}: {}", to, e.getMessage());
        }
    }
}