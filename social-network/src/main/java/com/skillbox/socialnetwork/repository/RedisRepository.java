package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.SaveMessage;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RedisRepository {

    Map<Object, Object> findAllMessages();

    void add(SaveMessage movie);

    void delete(String id);

    SaveMessage findMessage(String id);
}
