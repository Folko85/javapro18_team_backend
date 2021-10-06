package com.skillbox.socialnetwork.api.response.AuthDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;

@Data
public class UserRest {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private long dateAndTimeOfRegistration;
    @JsonProperty("birth_date")
    private long birthday;
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
    private long lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private String error;

}
