package com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusFriend implements Dto {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("status")
    private FriendshipStatusCode friendshipStatusCode;


}
