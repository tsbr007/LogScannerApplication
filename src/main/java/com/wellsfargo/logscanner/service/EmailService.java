package com.wellsfargo.logscanner.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wellsfargo.logscanner.utils.EmailNotification;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SimpleMailMessage templateMessage;
    
    private static final Logger logger = LoggerFactory.getLogger(LogScannerService.class);


    @Value("${email.recipient}")
    private String recipientEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender, SimpleMailMessage templateMessage) {
        this.mailSender = mailSender;
        this.templateMessage = templateMessage;
    }

    public void sendEmail(String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage(templateMessage);
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        logger.info("Email Sent successfully");
    }

    public void sendEmailNotification(EmailNotification notification) {
        sendEmail(notification.getSubject(), notification.getContent());
    }
}
