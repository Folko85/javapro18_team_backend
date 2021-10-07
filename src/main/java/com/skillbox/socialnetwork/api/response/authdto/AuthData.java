package com.skillbox.socialnetwork.api.response.authdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthData {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private LocalDateTime regDate;
    @JsonProperty("birth_date")
    private long birthDate;
    @JsonProperty("email")
    private String eMail;
    private String phone;
    private String photo;
    private String about;
    private Place city;
    private Place country;
    @JsonProperty("messages_permission")
    private String messagesPermission;
    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private String token;
}
