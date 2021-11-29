package com.skillbox.socialnetwork.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository implements RedisRepository {

    private static final String KEY = "Session";

    private final RedisTemplate<String, UUID> redisTemplate;

    private ZSetOperations<String, UUID> zSetOperations;

    public SessionRepository(RedisTemplate<String, UUID> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    public List<UUID> findAll() {
        return Objects.requireNonNull(zSetOperations.range(KEY, 0, 100_000)).stream().toList();
    }

    @Override
    public Optional<UUID> findByUserId(Integer userId) {
        return Objects.requireNonNull(zSetOperations.rangeByScore(KEY, userId, userId)).stream().findFirst();

    }

    @Override
    public void save(Integer userId, UUID sessionId) {
        deleteByUserId(userId);
        zSetOperations.add(KEY, sessionId, userId);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        Objects.requireNonNull(zSetOperations.rangeByScore(KEY, userId, userId))
                .forEach(uuid -> zSetOperations.remove(KEY, uuid));
    }

    @Override
    public Optional<Integer> findByUserUUID(UUID userUUID) {
        return Objects.requireNonNull(zSetOperations.rangeWithScores(KEY, 0, 100_000)).stream()
                .filter(x -> Objects.equals(x.getValue(), userUUID))
                .map(x -> Objects.requireNonNull(x.getScore()).intValue()).findFirst();
    }
}
