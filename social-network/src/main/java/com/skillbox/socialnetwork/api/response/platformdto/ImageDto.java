package com.skillbox.socialnetwork.api.response.platformdto;

import com.skillbox.socialnetwork.api.response.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageDto implements Dto {

    @Schema(description = "Идентификатор изображения")
    private String id;
    @Schema(description = "Имя файла")
    private String url;
}


