package com.skillbox.socialnetwork.api.response.socketio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Данные автора.
 */
@Data
@Accessors(chain = true)
public class AuthorData {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String photo;
}
