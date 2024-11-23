package com.laterna.xaxaxa.entity;

import com.laterna.xaxaxa.types.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "notification_type")
    private NotificationType notificationType;
    
    @Column(name = "sent")
    private boolean sent;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "additional_data")
    private String additionalData;
}