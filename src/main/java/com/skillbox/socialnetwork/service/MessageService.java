package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.DialogData;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.entity.Message;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Person2Dialog;
import com.skillbox.socialnetwork.repository.MessageRepository;
import com.skillbox.socialnetwork.repository.Person2DialogRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Service
public class MessageService {
    private final PersonRepository personRepository;
    private final MessageRepository messageRepository;
    private final Person2DialogRepository person2DialogRepository;

    public MessageService(PersonRepository personRepository, MessageRepository messageRepository, Person2DialogRepository person2DialogRepository) {
        this.personRepository = personRepository;
        this.messageRepository = messageRepository;
        this.person2DialogRepository = person2DialogRepository;
    }

    public ListResponse getMessages(int id, String query, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Person2Dialog person2Dialog = person2DialogRepository.findPerson2DialogByDialogIdAndPersonId(id, person.getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Message> messagePage = messageRepository.findMessagesByDialogIdAndTimeAfter(id, person2Dialog.getAddTime(), pageable);
        return getDialogResponse(offset, itemPerPage, messagePage, person2Dialog.getLastCheckTime());
    }

    private ListResponse getDialogResponse(int offset, int itemPerPage, Page<Message> messagePage, LocalDateTime checkTime) {
        ListResponse dialogResponse = new ListResponse();
        dialogResponse.setPerPage(itemPerPage);
        dialogResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dialogResponse.setOffset(offset);
        dialogResponse.setTotal((int) messagePage.getTotalElements());
        dialogResponse.setData(getDialogs4Response(messagePage.toList(), checkTime));
        return dialogResponse;
    }

    private List<Dto> getDialogs4Response(List<Message> messagePage, LocalDateTime checkTime) {
        List<Dto> dialogDataList = new ArrayList<>();
        messagePage.forEach(message -> {
            MessageData messageData = getMessageData(message, checkTime);
            dialogDataList.add(messageData);
        });
        return dialogDataList;
    }

    public MessageData getMessageData(Message message, LocalDateTime checkTime) {
        MessageData messageData = new MessageData();
        messageData.setMessageText(message.getText())
                .setAuthorId(message.getAuthor().getId())
                .setId(message.getId())
                .setTime(message.getTime().toInstant(UTC))
                .setReadStatus(message.getTime().isAfter(checkTime) ? "SENT" : "READ").setSendByMe(true);
        return messageData;
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }
}
