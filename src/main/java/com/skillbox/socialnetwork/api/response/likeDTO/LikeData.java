package com.skillbox.socialnetwork.api.response.likeDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeData implements Dto {
    private String likes;
    List<Integer> users;
}
