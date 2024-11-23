package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySentFalseAndUserIdIn(Collection<Long> userIds);
}