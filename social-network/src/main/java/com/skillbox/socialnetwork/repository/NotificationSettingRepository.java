package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Integer> {
    Optional<NotificationSetting> findNotificationSettingByPersonId(int personId);
}