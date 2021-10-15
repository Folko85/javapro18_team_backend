package com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import lombok.Data;

@Data
public class StatusFriend {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("status")
    private FriendshipStatusCode friendshipStatusCode;

    public StatusFriend() {
    }

    public StatusFriend(int userId, FriendshipStatusCode friendshipStatusCode) {
        this.userId = userId;
        this.friendshipStatusCode = friendshipStatusCode;
    }


}
