package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.model.SupportRequestDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.SendMessageDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.TechnicalSupportClientDto;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PusherService {
    private final QueueMessagingTemplate template;

    @Value("${message.queue.outgoing}")
    private String queueName;

    public AccountResponse createAndSendMessage(SupportRequestDto requestDto) {
        LocalDateTime dateOfApplication = LocalDateTime.now();

        TechnicalSupportClientDto client = new TechnicalSupportClientDto();
        client.setFirstName(requestDto.getFirstName());
        client.setLastName(requestDto.getLastName());
        client.setEMail(requestDto.getEmail());

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setDateOfApplication(dateOfApplication);
        sendMessageDto.setClient(client);
        sendMessageDto.setMessage(requestDto.getMessage());

        template.convertAndSend(queueName, sendMessageDto);
        log.info("Send new m: {}", sendMessageDto);
        AccountResponse response = new AccountResponse();
        response.setData(Map.of("message", "ok"));
        response.setTimestamp(Instant.now());
        return response;
    }

}
