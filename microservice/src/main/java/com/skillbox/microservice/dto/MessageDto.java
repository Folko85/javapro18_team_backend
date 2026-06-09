package com.skillbox.microservice.dto;

import lombok.Data;

/**
 * ДТО сообщения.
 */
@Data
public class MessageDto {

    private Integer id;
    private String clientEmail;
    private String messageText;

}
