package com.skillbox.socialnetwork.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Список городов")
public class CityDTO {

    @Schema (description = "Идентификатор города")
    int id;
    @Schema (description = "Название города города")
    String title;
}
