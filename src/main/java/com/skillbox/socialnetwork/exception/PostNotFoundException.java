package com.skillbox.socialnetwork.exception;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNotFoundException extends Exception{

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public PostNotFoundException(String s) {
        error = "invalid_request";
        this.errorDescription = s;
    }
}
