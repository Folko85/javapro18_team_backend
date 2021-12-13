package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.SupportRequestDto;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PusherService {
    private final RabbitTemplate template;

    public AccountResponse createAndSendMessage(SupportRequestDto requestDto) {

        String exchange = "support";
        String routingKey = "support";

        template.convertAndSend(exchange, routingKey, requestDto);
        log.info("Send new m: {}", requestDto);
        AccountResponse response = new AccountResponse();
        response.setData(Map.of("message", "ok"));
        response.setTimestamp(Instant.now());
        return response;
    }

}
