package com.laterna.xaxaxa.task;

import com.laterna.xaxaxa.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;

    @Scheduled(fixedDelayString = "${notification.processing.interval:60000}")
    public void processNotifications() {
        log.info("Starting notification processing");
        try {
            notificationService.processAndSendPendingNotifications();
        } catch (Exception e) {
            log.error("Error during notification processing", e);
        }
        log.info("Finished notification processing");
    }
}