package com.skillbox.microservice.service;

import com.skillbox.microservice.dto.MessageDto;
import com.skillbox.microservice.dto.TechnicalSupportClientDto;
import com.skillbox.microservice.entity.Client;
import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.repository.ClientRepository;
import com.skillbox.microservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;

    public String messageOutput(Model model) {
        Iterable<Message> messageIterable = messageRepository.findAll();
        ArrayList<Message> messageList = new ArrayList<>();
        for (Message message : messageIterable) {
            messageList.add(message);
        }
        model.addAttribute("messageList", messageList);
        model.addAttribute("messagesCount", messageList.size());
        return "result";
    }

    public void saveMessage(MessageDto dto) {

        Client c = getClientFromMessageDto(dto);
        Optional<Client> clientOptional = clientRepository.findByEmail(dto.getClient().getEMail());

        Client client = clientOptional.orElseGet(() -> clientRepository.save(c));
        Message messageOfSupport = getMessageFromMessageDto(dto, client);

        messageRepository.save(messageOfSupport);
    }

    private Client getClientFromMessageDto(MessageDto dto) {
        Client client = new Client();
        TechnicalSupportClientDto technicalSupportClientDto = dto.getClient();
        client.setFirstName(technicalSupportClientDto.getFirstName());
        client.setLastName(technicalSupportClientDto.getLastName());
        client.setEmail(technicalSupportClientDto.getEMail());
        return client;
    }

    private Message getMessageFromMessageDto(MessageDto dto, Client client) {
        Message messageOfSupport = new Message();
        messageOfSupport.setDateOfApplication(dto.getDateOfApplication());
        messageOfSupport.setClient(client);
        messageOfSupport.setMessage(dto.getMessage());
        return messageOfSupport;
    }


}
