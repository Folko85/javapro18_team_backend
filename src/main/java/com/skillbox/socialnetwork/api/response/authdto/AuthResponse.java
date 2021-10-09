package com.skillbox.socialnetwork.api.response.authdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String error;
    private Instant timestamp;
    private AuthData data;
}
