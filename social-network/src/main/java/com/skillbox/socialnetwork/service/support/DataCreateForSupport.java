package com.skillbox.socialnetwork.service.support;

import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class DataCreateForSupport {

    private Principal principal;
    private String message;

    public void setParam(String message, Principal principal) {
        this.message = message;
        this.principal = principal;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public String getMessage() {
        return message;
    }
}
