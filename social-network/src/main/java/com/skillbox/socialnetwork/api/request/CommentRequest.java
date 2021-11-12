package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.platformdto.ImageDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentRequest {
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("comment_text")
    private String commentText;
    @JsonProperty("images")
    private List<ImageDto> images;
}
