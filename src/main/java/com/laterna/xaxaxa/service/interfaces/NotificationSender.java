package com.laterna.xaxaxa.service.interfaces;

import com.laterna.xaxaxa.entity.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification) throws Exception;
}