package com.skillbox.socialnetwork.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class SaveMessage {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
