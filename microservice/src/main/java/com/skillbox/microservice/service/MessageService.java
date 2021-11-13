package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.MessageDto;
import com.skillbox.microservice.entity.Client;
import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.repository.ClientRepository;
import com.skillbox.microservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;

    public MessageService(MessageRepository messageRepository, ClientRepository clientRepository) {
        this.messageRepository = messageRepository;
        this.clientRepository = clientRepository;
    }

    public void saveMessage(MessageDto dto) {
        Client c = new Client();
        c.setFirstName(dto.getClient().getFirstName());
        c.setLastName(dto.getClient().getLastName());
        c.setEmail(dto.getClient().getEMail());

        Optional<Client> clientOptional = clientRepository.findByEmail(dto.getClient().getEMail());

        Client client;

        client = clientOptional.orElseGet(() -> clientRepository.save(c));

        Message messageOfSupport = new Message();
        messageOfSupport.setDateOfApplication(dto.getDateOfApplication());
        messageOfSupport.setClient(client);
        messageOfSupport.setMessage(dto.getMessage());
        messageRepository.save(messageOfSupport);
    }
}
