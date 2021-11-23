package com.skillbox.socialnetwork.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedisRepository {

    List<UUID> findAll();

    Optional<UUID> findByUserId(Integer userId);

    void save(Integer userId, UUID sessionId);

    void deleteByUserId(Integer userId);

    Optional<Integer> findByUserUUID(UUID userUUID);
}
