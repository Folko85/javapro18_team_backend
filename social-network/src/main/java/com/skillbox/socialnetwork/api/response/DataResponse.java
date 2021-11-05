package com.skillbox.socialnetwork.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import lombok.Data;

import java.time.Instant;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T extends Dto> {
    private String error;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant timestamp;
    private T data;
}
