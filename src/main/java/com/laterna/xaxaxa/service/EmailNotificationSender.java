package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.entity.Notification;
import com.laterna.xaxaxa.service.interfaces.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

//    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(Notification notification) throws Exception {
        log.info("Sending email notification: {}", notification);
    }
}