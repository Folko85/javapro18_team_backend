package com.skillbox.socialnetwork.api.response.postdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PostResponse {
    private String error;
    private long timestamp;
    private int total;
    private int offset;
    private int perPage;
    private List<?> data;
}
