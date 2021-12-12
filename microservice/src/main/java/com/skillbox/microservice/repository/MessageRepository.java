package com.skillbox.microservice.repository;

import com.skillbox.microservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("from Message m where m.id = ?1")
    Optional<Message> findMessageById(int id);

}
