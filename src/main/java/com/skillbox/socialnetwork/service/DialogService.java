package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.DialogRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.DialogData;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.repository.DialogRepository;
import com.skillbox.socialnetwork.repository.Person2DialogRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import static java.time.ZoneOffset.UTC;

@Service
public class DialogService {
    private final PersonRepository personRepository;
    private final Person2DialogRepository person2DialogRepository;
    private final DialogRepository dialogRepository;

    public DialogService(PersonRepository personRepository, Person2DialogRepository person2DialogRepository, DialogRepository dialogRepository) {
        this.personRepository = personRepository;
        this.person2DialogRepository = person2DialogRepository;
        this.dialogRepository = dialogRepository;
    }


    public ListResponse getDialogs(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person2Dialog> person2DialogPage = person2DialogRepository.findDialogByAuthorAndTitle(text, person.getId(), pageable);
        return getDialogResponse(offset, itemPerPage, person2DialogPage);
    }

    public DataResponse postDialog(DialogRequest dialogRequest, Principal principal) {
        Person currentPerson = findPerson(principal.getName());
        Dialog dialog = new Dialog();
        List<Person> personList = personRepository.findAllById(dialogRequest.getUserIds());
        if (personList.size() != dialogRequest.getUserIds().size())
            throw new UsernameNotFoundException("");
        dialog.setTitle("Новый чат");
        dialog = dialogRepository.save(dialog);
        Dialog finalDialog = dialog;
        List<Person2Dialog> person2DialogList = new ArrayList<>();
        personList.forEach(person -> {
            Person2Dialog person2Dialog = new Person2Dialog();
            person2Dialog.setDialog(finalDialog);
            person2Dialog.setPerson(person);
            person2Dialog.setAddTime(LocalDateTime.now(UTC));
            person2Dialog.setLastCheckTime(LocalDateTime.now(UTC));
            person2DialogList.add(person2Dialog);
        });
        person2DialogRepository.saveAll(person2DialogList);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setTimestamp(Instant.now());
        DialogData dialogData = new DialogData();
        dialogData.setId(dialog.getId());
        dataResponse.setData(dialogData);
        return dataResponse;
    }

    private ListResponse getDialogResponse(int offset, int itemPerPage, Page<Person2Dialog> person2DialogPage) {
        ListResponse dialogResponse = new ListResponse();
        dialogResponse.setPerPage(itemPerPage);
        dialogResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dialogResponse.setOffset(offset);
        dialogResponse.setTotal((int) person2DialogPage.getTotalElements());
        dialogResponse.setData(getDialogs4Response(person2DialogPage.toList()));
        return dialogResponse;
    }

    private List<Dto> getDialogs4Response(List<Person2Dialog> person2Dialogs) {
        List<Dto> dialogDataList = new ArrayList<>();
        person2Dialogs.forEach(person2Dialog -> {
            DialogData dialogDataData = getDialogData(person2Dialog);
            dialogDataList.add(dialogDataData);
        });
        return dialogDataList;
    }

    private DialogData getDialogData(Person2Dialog person2Dialog) {
        DialogData dialogData = new DialogData();
        dialogData.setId(person2Dialog.getDialog().getId());
        dialogData.setUnreadCount(person2Dialog.getDialog().getMessages()
                .stream().filter(message -> message.getTime().isAfter(person2Dialog.getLastCheckTime())).count());
        if (person2Dialog.getDialog().getMessages().size() > 0)
            dialogData.setLastMessage(getMessageData(person2Dialog.getDialog().getMessages()
                    .stream().max(Comparator.comparingInt(Message::getId)).get(), person2Dialog.getLastCheckTime()));
        return dialogData;
    }

    private MessageData getMessageData(Message message, LocalDateTime checkTime) {
        MessageData messageData = new MessageData();
        messageData.setMessageText(message.getText())
                .setAuthorId(message.getAuthor().getId())
                .setId(message.getId())
                .setTime(message.getTime().toInstant(UTC))
                .setReadStatus(message.getTime().isAfter(checkTime) ? "SENT" : "READ")
                .setRecipientId(1);
        return messageData;
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
