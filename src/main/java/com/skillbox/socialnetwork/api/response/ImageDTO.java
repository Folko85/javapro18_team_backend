package com.skillbox.socialnetwork.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Schema(description = "Данные о загруженном изображении")
public class ImageDTO {
    String error;
    Instant timestamp;
    Map data;
//        "id":"string",
//                "ownerId":12,
//                "fileName":"string",
//                "relativeFilePath":"string",
//                "rawFileURL":"string",
//                "fileFormat":"string",
//                "bytes":0,
//                "fileType":"IMAGE",
//                "createdAt":0

}


