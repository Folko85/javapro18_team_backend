package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.SupportRequestDto;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.config.property.RabbitProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
public class SupportService {
    private final RabbitTemplate template;
    private final RabbitProperties properties;

    public DataResponse<SuccessResponse> createAndSendMessage(SupportRequestDto requestDto) {
        template.convertAndSend(properties.getExchange(), properties.getRoutingKey(), requestDto);
        log.info("Send new m: {}", requestDto);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));
    }

}
