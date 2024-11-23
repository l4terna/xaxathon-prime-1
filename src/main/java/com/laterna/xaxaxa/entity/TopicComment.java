package com.laterna.xaxaxa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "topic_comments")
public class TopicComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
}
