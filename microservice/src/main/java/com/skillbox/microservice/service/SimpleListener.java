package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.MessageDto;
import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.entity.Client;
import com.skillbox.microservice.repository.ClientRepository;
import com.skillbox.microservice.repository.MessageRepository;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SimpleListener {
    private final MessageService messageService;

    public SimpleListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @SqsListener("${message.queue.incoming}")
    public void getMessage(MessageDto dto) {
        log.info("получено сообщение '{}' в support", dto.getMessage());
            messageService.saveMessage(dto);
        log.info("Received new message: {}", dto);

    }
}
