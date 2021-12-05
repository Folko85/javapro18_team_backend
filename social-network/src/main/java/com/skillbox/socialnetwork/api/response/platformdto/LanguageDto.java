package com.skillbox.socialnetwork.api.response.platformdto;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LanguageDto implements Dto {
    private int id;
    private String title;
}
