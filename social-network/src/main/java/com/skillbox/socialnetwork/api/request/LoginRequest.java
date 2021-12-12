package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("email")
    private String eMail;
    private String password;
}
