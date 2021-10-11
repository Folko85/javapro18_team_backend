package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DialogRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m " +
            "FROM Message m " +
            "LEFT JOIN Person p ON p.id = m.author.id " +
            "LEFT JOIN Person p2 ON p2.id = m.recipient.id " +
            "WHERE p.isBlocked = false AND p.id = ?2  AND  p2.firstName LIKE  %?1% ")
    Page<Message> findMessageByAuthor(String name, int personId, Pageable pageable);
}
