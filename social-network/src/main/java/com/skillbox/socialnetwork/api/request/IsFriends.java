package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Друзья.
 */
@Data
public class IsFriends {

    @JsonProperty("user_ids")
    private List<Integer> userIds;
}
