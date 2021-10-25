package com.skillbox.socialnetwork.api.socketiodto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageSocketDTO {
    private Integer id;
    private String time;
    private int author;
    private String text;
    private int dialog;
}
