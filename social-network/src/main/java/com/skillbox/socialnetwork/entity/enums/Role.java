package com.skillbox.socialnetwork.entity.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;

public enum Role {
    USER(of(Permission.USER)),
    MODERATOR(of(Permission.USER, Permission.MODERATE)),
    ADMIN(of(Permission.USER, Permission.MODERATE, Permission.ADMIN));
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream().map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
    }
}
