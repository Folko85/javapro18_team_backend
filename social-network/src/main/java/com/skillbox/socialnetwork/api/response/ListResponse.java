package com.skillbox.socialnetwork.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResponse<T extends Dto> {
    private String error;
    private Instant timestamp;
    private int total;
    private int offset;
    private int perPage;
    private List<T> data;
}
