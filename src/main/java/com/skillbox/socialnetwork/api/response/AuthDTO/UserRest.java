package com.skillbox.socialnetwork.api.response.AuthDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRest {
    @JsonProperty("first_name")
    private String firstName;
    private long timestamp;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private Instant dateAndTimeOfRegistration;
    @JsonProperty("birth_date")
    private Instant birthDate;
    @JsonProperty("last_online_time")
    private long lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
}
