package com.skillbox.socialnetwork.api.response.friendsdto;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

import java.util.List;

@Data
public class ResponseFriendsList implements Dto {
    private List<StatusFriend> data;
}
