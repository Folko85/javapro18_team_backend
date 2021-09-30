package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetFriendsListRequest {

    private String name;
    private Integer offset;
    @JsonProperty(value = "itemPerPage", defaultValue = "20")
    private Integer itemPerPage;
}
