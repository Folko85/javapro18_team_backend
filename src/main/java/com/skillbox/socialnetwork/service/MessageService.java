package com.skillbox.socialnetwork.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.skillbox.socialnetwork.api.request.MessageRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.entity.Message;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Person2Dialog;
import com.skillbox.socialnetwork.repository.MessageRepository;
import com.skillbox.socialnetwork.repository.Person2DialogRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.SessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Service
public class MessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final PersonRepository personRepository;
    private final MessageRepository messageRepository;
    private final Person2DialogRepository person2DialogRepository;
    private final SessionTemplate sessionTemplate;
    private final SocketIOServer server;

    public MessageService(PersonRepository personRepository,
                          MessageRepository messageRepository,
                          Person2DialogRepository person2DialogRepository,
                          SimpMessagingTemplate messagingTemplate,
                          SessionTemplate sessionTemplate,
                          SocketIOServer server) {
        this.personRepository = personRepository;
        this.messageRepository = messageRepository;
        this.person2DialogRepository = person2DialogRepository;
        this.messagingTemplate = messagingTemplate;
        this.sessionTemplate = sessionTemplate;
        this.server = server;
    }

    public ListResponse getMessages(int id, String query, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Person2Dialog person2Dialog = person2DialogRepository.findPerson2DialogByDialogIdAndPersonId(id, person.getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Message> messagePage = messageRepository.findMessagesByDialogIdAndTimeAfter(id, person2Dialog.getAddTime(), pageable);
        person2Dialog.setLastCheckTime(LocalDateTime.now());
        person2DialogRepository.save(person2Dialog);
        return getDialogResponse(offset, itemPerPage, messagePage, person2Dialog);
    }

    public DataResponse postMessage(int id, MessageRequest messageRequest, Principal principal) {
        Person person = findPerson(principal.getName());
        Person2Dialog person2Dialog = person2DialogRepository.findPerson2DialogByDialogIdAndPersonId(id, person.getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        DataResponse dataResponse = new DataResponse();
        dataResponse.setTimestamp(Instant.now());
        Message message = new Message();
        message.setAuthor(person);
        message.setDialog(person2Dialog.getDialog());
        message.setTime(LocalDateTime.now());
        message.setText(messageRequest.getMessageText());
        message = messageRepository.save(message);
        dataResponse.setData(getMessageData(message, person2Dialog));
        sessionTemplate.findByUserId(message.getDialog().getPersons().stream()
                .filter(person1 -> !person1.getId().equals(person.getId())).findFirst().get().getId())
                .ifPresent(uuid -> server.getClient(uuid).sendEvent("message", dataResponse));
        //  messagingTemplate.convertAndSendToUser(message.getDialog().toString(), "/queue/messages", message);

        return dataResponse;
    }

    private ListResponse getDialogResponse(int offset, int itemPerPage, Page<Message> messagePage, Person2Dialog person2Dialog) {
        ListResponse dialogResponse = new ListResponse();
        dialogResponse.setPerPage(itemPerPage);
        dialogResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dialogResponse.setOffset(offset);
        dialogResponse.setTotal((int) messagePage.getTotalElements());
        dialogResponse.setData(getDialogs4Response(messagePage.toList(), person2Dialog));
        return dialogResponse;
    }

    private List<Dto> getDialogs4Response(List<Message> messagePage, Person2Dialog person2Dialog) {
        List<Dto> dialogDataList = new ArrayList<>();
        messagePage.forEach(message -> {
            MessageData messageData = getMessageData(message, person2Dialog);
            dialogDataList.add(messageData);
        });
        return dialogDataList;
    }

    public MessageData getMessageData(Message message, Person2Dialog person2Dialog) {
        MessageData messageData = new MessageData();
        messageData.setMessageText(message.getText())
                .setAuthorId(message.getAuthor().getId())
                .setId(message.getId())
                .setTime(message.getTime().toInstant(UTC))
                .setReadStatus(message.getTime().isAfter(person2Dialog.getLastCheckTime()) ? "SENT" : "READ")
                .setSendByMe(person2Dialog.getPerson().equals(message.getAuthor()));
        return messageData;
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
