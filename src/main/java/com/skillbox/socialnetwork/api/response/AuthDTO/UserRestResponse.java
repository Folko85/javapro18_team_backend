package com.skillbox.socialnetwork.api.response.AuthDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRestResponse {
    private String error;
    private long timestamp;
    private UserRest data;
}
