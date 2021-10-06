package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateWithInstantRequestModel {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private Instant birthday;
    private String phone;
    private String photo_id;
    private String about;
    @JsonIgnoreProperties("city")
    private Place city;
    @JsonIgnoreProperties("country")
    private Place country;
    @JsonProperty("messages_permission")
    private MessagesPermission messagesPermission;
}
