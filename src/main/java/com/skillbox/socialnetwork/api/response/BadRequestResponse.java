package com.skillbox.socialnetwork.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BadRequestResponse {
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
}
