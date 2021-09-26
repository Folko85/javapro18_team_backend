package com.skillbox.socialnetwork.api.response.FriendsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendsList {

    @JsonProperty(defaultValue = "string")
    private String error;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("perPage")
    private Integer perPage;

    @JsonProperty("data")
    private Set<FriendsPojo> friends;
}
