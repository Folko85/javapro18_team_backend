package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.SendMessageDto;
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

    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;

    public SimpleListener(MessageRepository messageRepository, ClientRepository clientRepository) {
        this.messageRepository = messageRepository;
        this.clientRepository = clientRepository;
    }

    @SqsListener("queueFromJavaCode")
    public void getMessage(SendMessageDto dto) {
        Client c = new Client();
        c.setFirstName(dto.getClient().getFirstName());
        c.setLastName(dto.getClient().getLastName());
        c.setEMail(dto.getClient().getEMail());

        Optional<Client> clientOptional = clientRepository.findByEMail(dto.getClient().getEMail());

        Client client;

        client = clientOptional.orElseGet(() -> clientRepository.save(c));

        Message messageOfSupport = new Message();
        messageOfSupport.setDateOfApplication(dto.getDateOfApplication());
        messageOfSupport.setClient(client);
        messageOfSupport.setMessage(dto.getMessage());
        messageRepository.save(messageOfSupport);

        log.info("Received new message: {}", dto);

    }
}
