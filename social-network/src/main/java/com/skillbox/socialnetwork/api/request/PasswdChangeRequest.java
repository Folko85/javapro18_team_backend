package com.skillbox.socialnetwork.api.request;

import lombok.Data;

/**
 * Изменение пароля.
 */
@Data
public class PasswdChangeRequest {
    private String token;
    private String password;
}
