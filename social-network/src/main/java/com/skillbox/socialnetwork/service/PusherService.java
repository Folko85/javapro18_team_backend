package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.technicalSupportDto.SendMessageDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.TechnicalSupportClientDto;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PusherService {
    private final QueueMessagingTemplate template;
//    private MessageOfTechnicalSupportClient messageOfTechnicalSupportClient;

    @Value("${message.queue.outgoing}")
    private String queueName;
    private String fName;
    private String lName;
    private String email;
    private String text;

    public PusherService(QueueMessagingTemplate template) {
        this.template = template;
    }

    public void setParam(String fName, String lName, String email, String text) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.text = text;
    }

//    public void setParam(MessageOfTechnicalSupportClient messageOfTechnicalSupportClient) {
//        this.messageOfTechnicalSupportClient = messageOfTechnicalSupportClient;
//    }

    public void createAndSendMessage() {
        LocalDateTime dateOfApplication = LocalDateTime.now();

        TechnicalSupportClientDto client = new TechnicalSupportClientDto();
        client.setFirstName(fName);
        client.setLastName(lName);
        client.setEMail(email);

        String message = text;

//        client.setFirstName(messageOfTechnicalSupportClient.getFirstName());
//        client.setLastName(messageOfTechnicalSupportClient.getLastName());
//        client.setEMail(messageOfTechnicalSupportClient.getEMail());
//
//        String message = messageOfTechnicalSupportClient.getMessage();

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setDateOfApplication(dateOfApplication);
        sendMessageDto.setClient(client);
        sendMessageDto.setMessage(message);

        template.convertAndSend(queueName, sendMessageDto);
        log.info("Send new m: {}", sendMessageDto);
    }

}
