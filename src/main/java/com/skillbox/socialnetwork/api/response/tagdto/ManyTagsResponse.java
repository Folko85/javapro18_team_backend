package com.skillbox.socialnetwork.api.response.tagdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.socialnetwork.entity.Tag;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManyTagsResponse {

    private String error;
    private Instant timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<Tag> data;
}
