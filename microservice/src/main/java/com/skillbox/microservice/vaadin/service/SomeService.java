package com.skillbox.microservice.vaadin.service;

import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SomeService {
    private final MessageRepository messageRepository;

    public SomeService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void deleteMessage(Message message) {
        Optional<Message> m = messageRepository.findMessageById(message.getId());
        if (m.isPresent()) {
            messageRepository.deleteById(message.getId());
        }
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
