package com.skillbox.socialnetwork.api.response.authDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthData implements Dto {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private Instant regDate;
    @JsonProperty("birth_date")
    private Instant birthDate;
    @JsonProperty("email")
    private String eMail;
    private String phone;
    private String photo;
    private String about;
    private Place city;
    private Place country;
    @JsonProperty("messages_permission")
    private MessagesPermission messagesPermission;
    @JsonProperty("last_online_time")
    private Instant lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private String token;

}
