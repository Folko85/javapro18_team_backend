package com.skillbox.socialnetwork.api.request.model;

import lombok.Data;

@Data
public class SupportRequestDto {
    private String email;
    private String message;
    private String name;
}
