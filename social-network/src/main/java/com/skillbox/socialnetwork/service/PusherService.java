package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.technicalSupportDto.MessageOfTechnicalSupportClient;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.SendMessageDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.TechnicalSupportClientDto;
import com.skillbox.socialnetwork.repository.PersonRepository;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PusherService {
    private final QueueMessagingTemplate template;
    private final PersonRepository personRepository;
    private MessageOfTechnicalSupportClient messageOfTechnicalSupportClient;

    public PusherService(QueueMessagingTemplate template, PersonRepository personRepository) {
        this.template = template;
        this.personRepository = personRepository;
    }

    public void setParam(MessageOfTechnicalSupportClient messageOfTechnicalSupportClient) {
        this.messageOfTechnicalSupportClient = messageOfTechnicalSupportClient;
    }

    public void createAndSendMessage() {
        LocalDateTime dateOfApplication = LocalDateTime.now();

        TechnicalSupportClientDto client = new TechnicalSupportClientDto();
        client.setFirstName(messageOfTechnicalSupportClient.getFirstName());
        client.setLastName(messageOfTechnicalSupportClient.getLastName());
        client.setEMail(messageOfTechnicalSupportClient.getEMail());

        String message = messageOfTechnicalSupportClient.getMessage();

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setDateOfApplication(dateOfApplication);
        sendMessageDto.setClient(client);
        sendMessageDto.setMessage(message);

        template.convertAndSend("queueFromJavaCode", sendMessageDto);
        log.info("Send new m: {}", sendMessageDto);
    }

}
