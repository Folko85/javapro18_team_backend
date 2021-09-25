package com.skillbox.socialnetwork.api.response.AuthDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.entity.Message;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public void setBirthday(LocalDate birthday) {
        if(birthday!= null) {
            java.sql.Date date = java.sql.Date.valueOf(birthday);
            this.birthday = date.getTime() / 1000;
        }
    }

    public void setDateAndTimeOfRegistration(LocalDateTime dateAndTimeOfRegistration) {
        this.dateAndTimeOfRegistration = dateAndTimeOfRegistration.toEpochSecond(UTC);
    }

    public void setLastOnlineTime(LocalDateTime lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime.toEpochSecond(UTC);
    }
}
