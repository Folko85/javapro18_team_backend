package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Лайк.
 */
@Data
public class LikeRequest {
    @JsonProperty("item_id")
    private int itemId;
    private String type;

}
