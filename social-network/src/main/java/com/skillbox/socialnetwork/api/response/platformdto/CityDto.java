package com.skillbox.socialnetwork.api.response.platformdto;

import com.skillbox.socialnetwork.api.response.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Список городов")
public class CityDto implements Dto {

    @Schema (description = "Идентификатор города")
    int id;
    @Schema (description = "Название города города")
    String title;
}
