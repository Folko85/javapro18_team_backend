package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findByPersonIdAndReadStatusIsFalse(int personId, Pageable pageable);
}
