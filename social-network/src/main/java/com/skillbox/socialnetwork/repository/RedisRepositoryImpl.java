package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.SaveMessage;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "MESSAGES";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Object, Object> hashOperations;

    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * В этом методе мы используем функцию с именем opsForHash(),
     * она возвращает операции выполняемые с хеш-значениями,
     * привязанными к данному ключу.
     */
    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void add(final SaveMessage message) {
        hashOperations.put(KEY, message.getId(), message.getName());
    }

    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }

    public SaveMessage findMessage(final String id) {
        return (SaveMessage) hashOperations.get(KEY, id);
    }

    public Map<Object, Object> findAllMessages() {
        return hashOperations.entries(KEY);
    }
}
