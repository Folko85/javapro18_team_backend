package com.skillbox.socialnetwork.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class SessionRepository implements RedisRepository {

    private static final String KEY = "Session";

    private final RedisTemplate<String, UUID> redisTemplate;

    private ZSetOperations<String, UUID> zSetOperations;

    public SessionRepository(RedisTemplate<String, UUID> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public List<UUID> findAll() {
        return Objects.requireNonNull(zSetOperations.range(KEY, 0, 100_000)).stream().toList();
    }

    @Override
    public Optional<UUID> findByUserId(Integer userId) {
        Set<UUID> typedTuple = zSetOperations.range(KEY, userId, userId);
        return typedTuple.stream().findFirst();
    }

    @Override
    public void save(Integer userId, UUID sessionId) {
        zSetOperations.add(KEY, sessionId, userId);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        findByUserId(userId).ifPresent(uuid -> zSetOperations.remove(KEY, uuid));

    }

    @Override
    public Optional<Integer> findByUserUUID(UUID userUUID) {
        return Objects.requireNonNull(zSetOperations.rangeWithScores(KEY, 0, 100_000)).stream()
                .map(ZSetOperations.TypedTuple::getScore).filter(Objects::nonNull).map(Double::intValue).findFirst();
    }
}
