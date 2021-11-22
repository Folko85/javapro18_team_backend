package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findByPersonIdAndReadStatusIsFalse(int personId, Pageable pageable);
    List<Notification> findByPersonIdAndReadStatusIsFalse(int personId);
}
