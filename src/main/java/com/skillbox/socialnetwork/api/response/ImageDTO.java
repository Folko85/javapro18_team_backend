package com.skillbox.socialnetwork.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors (chain = true)
@Schema(description = "Данные о загруженном изображении")
public class ImageDTO implements Dto {

    private String id;
    private int ownerId;
    private String fileName;
    private String relativeFilePath;
    private String rawFileURL;
    private String fileFormat;
    private int bytes;
    private String fileType;
    private Instant createdAt;

}


