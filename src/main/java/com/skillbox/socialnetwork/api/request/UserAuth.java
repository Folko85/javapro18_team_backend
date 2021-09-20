package com.skillbox.socialnetwork.api.request;

import lombok.Data;

@Data
public class UserAuth {
    private String eMail;
    private String password;
}
