package com.skillbox.socialnetwork.api.response.postdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.socialnetwork.api.response.Dto;
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
    private Dto data;
}
