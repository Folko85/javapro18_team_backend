package com.skillbox.socialnetwork.api.response.dialogdto;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Количество.
 */
@Data
@Accessors(chain = true)
public class CountData implements Dto {
    private String count;
}
