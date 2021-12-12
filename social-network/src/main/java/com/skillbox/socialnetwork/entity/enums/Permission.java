package com.skillbox.socialnetwork.entity.enums;

public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate"),
    ADMIN("user:administrate");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
