package com.skillbox.microservice.service;

import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Некий сервис.
 */
@Service
@RequiredArgsConstructor
public class SomeService {
    private final MessageRepository messageRepository;

    /**
     * Метод удаления сообщения.
     * @param message - сообщение
     */
    public void deleteMessage(final Message message) {
        final Optional<Message> m = messageRepository.findMessageById(message.getId());
        if (m.isPresent()) {
            messageRepository.deleteById(message.getId());
        }
    }

    /**
     * Метод получения сообщения по id.
     * @param id
     * @return
     */
    public Message getMessageById(final Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
