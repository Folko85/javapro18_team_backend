package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.entity.SaveMessage;
import com.skillbox.socialnetwork.repository.RedisRepositoryImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RedisController {

    private final RedisRepositoryImpl redisRepositoryImpl;

    public RedisController(RedisRepositoryImpl redisRepositoryImpl) {
        this.redisRepositoryImpl = redisRepositoryImpl;
    }

    @PostMapping("/redis")
    public void save(@RequestParam(name = "id") String id,
                     @RequestParam(name = "message") String m) {
        SaveMessage saveMessage = new SaveMessage();
        saveMessage.setId(id);
        saveMessage.setName(m);
        redisRepositoryImpl.add(saveMessage);
    }

    @GetMapping("/redis/messages")
    public Map<String, String> get() {
        Map<Object, Object> allMessages = redisRepositoryImpl.findAllMessages();

        Map<String, String> messages = new HashMap<>();

        for (Map.Entry<Object, Object> entry : allMessages.entrySet()) {
            String key = (String) entry.getKey();
            messages.put(key, allMessages.get(key).toString());
        }
        return messages;
    }
}
