package com.skillbox.socialnetwork.api.response.likedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeData {
    private String likes;
    List<Integer> users;
}
