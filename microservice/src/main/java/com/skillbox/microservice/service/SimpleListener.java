package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.MessageDto;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleListener {
    private final MessageService messageService;

    @SqsListener("${message.queue.incoming}")
    public void getMessage(MessageDto dto) {
        log.info("получено сообщение '{}' в support", dto.getMessage());
        messageService.saveMessage(dto);
        log.info("Received new message: {}", dto);

    }
}
