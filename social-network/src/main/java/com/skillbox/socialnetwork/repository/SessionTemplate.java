package com.skillbox.socialnetwork.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionTemplate {
    private final static ConcurrentHashMap<Integer, UUID> template = new ConcurrentHashMap<>();

    /**
     * 获取所有SessionId
     *
     * @return SessionId列表
     */
    public List<UUID> findAll() {
        return new ArrayList<>(template.values());
    }

    /**
     * 根据UserId查询SessionId
     *
     * @param userId 用户id
     * @return SessionId
     */
    public Optional<UUID> findByUserId(Integer userId) {
        return Optional.ofNullable(template.get(userId));
    }

    /**
     * 保存/更新 user_id <-> session_id 的关系
     *
     * @param userId    用户id
     * @param sessionId SessionId
     */
    public void save(Integer userId, UUID sessionId) {
        template.put(userId, sessionId);
    }

    /**
     * 删除 user_id <-> session_id 的关系
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Integer userId) {
        template.remove(userId);
    }

}
