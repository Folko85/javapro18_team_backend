package com.skillbox.socialnetwork.api.response.tagdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteTagResponse {

        private String error;
        private Instant timestamp;
        private Map<String, String> data;

}
