package com.skillbox.socialnetwork.api.response.FriendsDTO;

import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Friends {
    private int id;

    private String firstName;

    private String lastName;

    private LocalDateTime regDate;

    private LocalDate birthDate;

    private String eMail;

    private String phone;

    private String photo;

    private String about;

    private String city;

    private String country;

    private MessagesPermission messagesPermission;

    private LocalDateTime lastOnlineTime;

    private boolean isBlocked;
}
