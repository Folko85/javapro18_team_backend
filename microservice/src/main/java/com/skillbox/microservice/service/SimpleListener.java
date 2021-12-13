package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.SupportRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleListener {
    private final MessageService messageService;

    @RabbitListener(queues = "support", messageConverter = "commonJsonMessageConverter")
    public void getMessage(SupportRequestDto dto) {
        log.info("получено сообщение '{}' в support", dto.getMessage());
        messageService.saveMessage(dto);
        log.info("Received new message: {}", dto);
    }
}
