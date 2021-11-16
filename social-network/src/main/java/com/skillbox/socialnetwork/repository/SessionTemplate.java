package com.skillbox.socialnetwork.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionTemplate {
    private final static ConcurrentHashMap<Integer, UUID> template = new ConcurrentHashMap<>();

    public List<UUID> findAll() {
        return new ArrayList<>(template.values());
    }

    public Optional<UUID> findByUserId(Integer userId) {
        return Optional.ofNullable(template.get(userId));
    }

    public void save(Integer userId, UUID sessionId) {
        template.put(userId, sessionId);
    }

    public void deleteByUserId(Integer userId) {
        template.remove(userId);
    }

    public Optional<Integer> findByUserUUID(UUID userUUID) {
        return template.keySet(userUUID).stream().findFirst();
    }

}
