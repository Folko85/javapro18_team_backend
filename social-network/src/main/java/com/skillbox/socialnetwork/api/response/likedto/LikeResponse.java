package com.skillbox.socialnetwork.api.response.likedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponse {
    private String error;
    private Instant timestamp;
    private LikeData data;
}