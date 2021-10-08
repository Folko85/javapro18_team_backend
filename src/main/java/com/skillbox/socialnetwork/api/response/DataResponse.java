package com.skillbox.socialnetwork.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse {
    private String error;
    private Instant timestamp;
    private Dto data;
}
