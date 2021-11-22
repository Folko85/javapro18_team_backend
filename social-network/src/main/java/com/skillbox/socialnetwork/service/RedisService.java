package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.entity.SaveMessage;
import com.skillbox.socialnetwork.repository.RedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisRepositoryImpl redisRepositoryImpl;

    public void save(String id, String message) {
        SaveMessage saveMessage = new SaveMessage();
        saveMessage.setId(id);
        saveMessage.setName(message);
        redisRepositoryImpl.add(saveMessage);
    }

    public Map<String, String> getAllMessages() {
        Map<Object, Object> allMessages = redisRepositoryImpl.findAllMessages();

        Map<String, String> messages = new HashMap<>();

        for (Map.Entry<Object, Object> entry : allMessages.entrySet()) {
            String key = (String) entry.getKey();
            messages.put(key, allMessages.get(key).toString());
        }
        return messages;
    }
}
