package com.skillbox.socialnetwork.api.response.authDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDeleteResponse {
    private String error;
    private long timestamp;
    private Map<String,String> data;
}
