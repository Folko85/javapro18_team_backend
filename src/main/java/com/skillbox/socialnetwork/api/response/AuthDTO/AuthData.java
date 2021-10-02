package com.skillbox.socialnetwork.api.response.AuthDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
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

    public AuthData(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.regDate = person.getDateAndTimeOfRegistration();
        this.birthDate = person.getBirthday().toEpochDay();
        this.eMail = person.getEMail();
        this.phone = person.getPhone();
        this.photo = person.getPhoto();
        this.about = person.getAbout();
//        this.city = person.getCity();
//        this.country = person.getCountry();
        this.messagesPermission = person.getMessagesPermission().toString();
        this.lastOnlineTime = person.getLastOnlineTime();
        this.isBlocked = person.isBlocked();
    }
}
