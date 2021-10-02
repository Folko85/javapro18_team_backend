package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDataResponse {
    private String error;
    private long timestamp;
    private Object data;

    public PostDataResponse(String error, Object data) {
        this.error = error;
        timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public PostDataResponse(Object data) {
        this("String", data);
    }
}
