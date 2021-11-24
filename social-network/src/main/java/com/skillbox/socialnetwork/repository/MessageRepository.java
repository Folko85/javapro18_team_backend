package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Page<Message> findMessagesByDialogIdAndTimeAfterOrderByTimeDesc(int dialog, LocalDateTime time, Pageable pageable);
}
