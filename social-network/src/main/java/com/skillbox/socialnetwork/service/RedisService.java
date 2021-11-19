package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.entity.SaveMessage;
import com.skillbox.socialnetwork.repository.MessagesRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisService {
    private final MessagesRepository messagesRepository;

    public RedisService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public void save(String id, String message) {
        SaveMessage saveMessage = new SaveMessage();
        saveMessage.setId(id);
        saveMessage.setName(message);
        messagesRepository.add(saveMessage);
    }

    public Map<String, String> getAllMessages() {
        Map<Object, Object> allMessages = messagesRepository.findAllMessages();

        Map<String, String> messages = new HashMap<>();

        for (Map.Entry<Object, Object> entry : allMessages.entrySet()) {
            String key = (String) entry.getKey();
            messages.put(key, allMessages.get(key).toString());
        }
        return messages;
    }
}
