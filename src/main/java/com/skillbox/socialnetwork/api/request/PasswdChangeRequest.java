package com.skillbox.socialnetwork.api.request;

import lombok.Data;

@Data
public class PasswdChangeRequest {
    private String token;
    private String password;
}
