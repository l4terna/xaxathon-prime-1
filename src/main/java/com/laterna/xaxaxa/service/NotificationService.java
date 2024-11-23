package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.entity.Notification;
import com.laterna.xaxaxa.entity.UserSettings;
import com.laterna.xaxaxa.repository.NotificationRepository;
import com.laterna.xaxaxa.repository.UserSettingsRepository;
import com.laterna.xaxaxa.service.interfaces.NotificationSender;
import com.laterna.xaxaxa.types.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final UserSettingsRepository userSettingsRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSender notificationSender;
    private final UserService userService;


    @Transactional
    public void createNotification(Long userId, NotificationType type, String title, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void createNotificationForAllUsers(NotificationType type, String title, String message) {
            createNotificationForUsers(type, title, message, userService.getAllUserIds());
    }

    @Transactional
    public void createNotificationForUsers(NotificationType type, String title, String message, List<Long> userIds) {
        if (userIds.isEmpty()) {
            log.warn("No users provided for notification creation");
            return;
        }

        List<Notification> notifications = userIds.stream()
                .map(userId -> Notification.builder()
                        .userId(userId)
                        .notificationType(type)
                        .title(title)
                        .message(message)
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
        log.info("Created {} notifications for users", notifications.size());
    }

    @Transactional
    public void createNotificationForUsersWithEnabledNotifications(NotificationType type, String title, String message) {
        List<Long> userIds = userSettingsRepository.findByNotificationsEnabledTrue()
                .stream()
                .map(UserSettings::getUserId)
                .collect(Collectors.toList());

        createNotificationForUsers(type, title, message, userIds);
    }

    @Transactional
    public void processAndSendPendingNotifications() {
        List<UserSettings> enabledUsers = userSettingsRepository.findByNotificationsEnabledTrue();
        if (enabledUsers.isEmpty()) {
            log.info("No users with enabled notifications found");
            return;
        }

        List<Long> enabledUserIds = enabledUsers.stream()
                .map(UserSettings::getUserId)
                .collect(Collectors.toList());

        List<Notification> pendingNotifications = notificationRepository
                .findBySentFalseAndUserIdIn(enabledUserIds);

        if (pendingNotifications.isEmpty()) {
            log.info("No pending notifications found");
            return;
        }

        for (Notification notification : pendingNotifications) {
            try {
                notificationSender.sendNotification(notification);
                
                notification.setSent(true);
                notification.setSentAt(LocalDateTime.now());
                notification.setUpdatedAt(LocalDateTime.now());
                notificationRepository.save(notification);
                
                log.info("Successfully sent notification id: {}", notification.getId());
            } catch (Exception e) {
                log.error("Failed to send notification id: {}", notification.getId(), e);
            }
        }
    }
}
