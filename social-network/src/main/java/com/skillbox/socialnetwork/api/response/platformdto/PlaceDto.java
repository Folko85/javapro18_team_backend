package com.skillbox.socialnetwork.api.response.platformdto;

import com.skillbox.socialnetwork.api.response.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors (chain = true)
@Schema(description = "Список городов")
public class PlaceDto implements Dto {

    @Schema (description = "Идентификатор города/страны")
    int id;
    @Schema (description = "Название города/страны")
    String title;
}
