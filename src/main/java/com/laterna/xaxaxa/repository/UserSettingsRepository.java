package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    List<UserSettings> findByNotificationsEnabledTrue();
}