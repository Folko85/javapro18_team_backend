package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/redis")
    public void save(@RequestParam(name = "id") String id,
                     @RequestParam(name = "message") String m) {
        redisService.save(id, m);
    }

    @GetMapping("/redis/messages")
    public Map<String, String> get() {
        return redisService.getAllMessages();
    }
}
