package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest {
    @JsonProperty("email")
    private String eMail;
    @JsonProperty("passwd1")
    private String password1;
    @JsonProperty("passwd2")
    private String password2;
    private String firstName;
    private String lastName;
    private String  code;
}
