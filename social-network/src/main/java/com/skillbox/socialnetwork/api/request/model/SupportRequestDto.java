package com.skillbox.socialnetwork.api.request.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupportRequestDto {
    @JsonProperty ("e_mail")
    private String email;
    private String message;
    @JsonProperty ("first_name")
    private String firstName;
    @JsonProperty ("last_name")
    private String lastName;
}