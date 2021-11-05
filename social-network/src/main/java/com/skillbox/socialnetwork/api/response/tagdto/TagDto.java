package com.skillbox.socialnetwork.api.response.tagdto;

import com.skillbox.socialnetwork.api.response.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors ( chain = true)
@Schema(description = "Список городов")
public class TagDto implements Dto {

    @Schema (description = "Идентификатор тэга")
    int id;
    @Schema (description = "Собственно тэг")
    String tag;
}
