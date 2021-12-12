package com.skillbox.socialnetwork.api.response.tagdto;

import com.skillbox.socialnetwork.api.response.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Список городов")
public class TagDto implements Dto {

    @Schema(description = "Идентификатор тэга")
    private int id;
    @Schema(description = "Собственно тэг")
    private String tag;
}
