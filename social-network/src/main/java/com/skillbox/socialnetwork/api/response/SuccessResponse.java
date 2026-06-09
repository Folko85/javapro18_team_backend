package com.skillbox.socialnetwork.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Успешный ответ.
 */
@Data
@Accessors(chain = true)
public class SuccessResponse implements Dto {
    private String message;
}
