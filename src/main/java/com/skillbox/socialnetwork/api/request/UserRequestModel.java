package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.authDTO.Place;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestModel {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private long birthday;
    private String phone;
    private String photo_id;
    private String about;
    private Place city;
    private Place country;
    private int country_id;
    @JsonProperty("messages_permission")
    private MessagesPermission messagesPermission;
}
