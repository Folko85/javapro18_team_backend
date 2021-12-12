package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.DialogRequest;
import com.skillbox.socialnetwork.api.request.socketio.ReadMessagesData;
import com.skillbox.socialnetwork.api.request.socketio.TypingData;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.DialogData;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.api.response.socketio.TypingResponse;
import com.skillbox.socialnetwork.entity.Dialog;
import com.skillbox.socialnetwork.entity.Message;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Person2Dialog;
import com.skillbox.socialnetwork.repository.DialogRepository;
import com.skillbox.socialnetwork.repository.Person2DialogRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
@AllArgsConstructor
public class DialogService {
    private final PersonRepository personRepository;
    private final Person2DialogRepository person2DialogRepository;
    private final DialogRepository dialogRepository;
    private final MessageService messageService;
    private final NotificationService notificationService;

    public ListResponse<DialogData> getDialogs(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person2Dialog> person2DialogPage = person2DialogRepository.findDialogByAuthorAndTitle(text, person.getId(), pageable);
        return getDialogResponse(offset, itemPerPage, person2DialogPage);
    }

    public DataResponse<DialogData> postDialog(DialogRequest dialogRequest, Principal principal) {
        Person currentPerson = findPerson(principal.getName());
        List<Person> personList = personRepository.findAllById(dialogRequest.getUsersIds());
        if (personList.size() != dialogRequest.getUsersIds().size())
            throw new UsernameNotFoundException("");
        Person personDst = personList.stream().findFirst().get();
        List<Dialog> dialogs = dialogRepository.findPerson2DialogByPersonDialog(currentPerson.getId(), personDst.getId());
        DialogData dialogData = new DialogData();
        if (dialogs.size() == 0) {
            Dialog dialog = new Dialog();
            personList.add(currentPerson);
            dialog.setTitle("Новый чат");
            dialog.setDialog(true);
            dialog = dialogRepository.save(dialog);
            Dialog finalDialog = dialog;
            List<Person2Dialog> person2DialogList = new ArrayList<>();
            personList.forEach(person -> {
                if (dialogRepository.findPerson2DialogByPersonDialog(currentPerson.getId(), person.getId()).size() > 0)
                    throw new EntityNotFoundException("");
                Person2Dialog person2Dialog = new Person2Dialog();
                person2Dialog.setDialog(finalDialog);
                person2Dialog.setPerson(person);
                person2Dialog.setAddTime(LocalDateTime.now(UTC));
                person2Dialog.setLastCheckTime(LocalDateTime.now(UTC));
                person2DialogList.add(person2Dialog);
            });
            person2DialogRepository.saveAll(person2DialogList);
            dialogData.setId(dialog.getId());
        } else dialogData.setId(dialogs.stream().findFirst().get().getId());

        DataResponse<DialogData> dataResponse = new DataResponse<>();
        dataResponse.setTimestamp(Instant.now());
        dialogData.setRecipientId(setAuthData(personDst));
        dataResponse.setData(dialogData);
        return dataResponse;
    }

    private ListResponse<DialogData> getDialogResponse(int offset, int itemPerPage, Page<Person2Dialog> person2DialogPage) {
        ListResponse<DialogData> dialogResponse = new ListResponse<>();
        dialogResponse.setPerPage(itemPerPage);
        dialogResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dialogResponse.setOffset(offset);
        dialogResponse.setTotal((int) person2DialogPage.getTotalElements());
        dialogResponse.setData(getDialogs4Response(person2DialogPage.toList()));
        return dialogResponse;
    }

    private List<DialogData> getDialogs4Response(List<Person2Dialog> person2Dialogs) {
        List<DialogData> dialogDataList = new ArrayList<>();
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
            dialogData.setLastMessage(messageService.getMessageData(person2Dialog.getDialog().getMessages()
                    .stream().max(Comparator.comparingInt(Message::getId)).get(), person2Dialog));
        else dialogData.setLastMessage(new MessageData());
        dialogData.setRecipientId(setAuthData(person2Dialog.getDialog().getPersons()
                .stream().filter(person -> !person.getId().equals(person2Dialog.getPerson().getId())).findFirst()
                .orElse(person2Dialog.getPerson())));
        return dialogData;
    }


    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    public void startTyping(TypingData typingData) {
        Optional<Dialog> dialog = dialogRepository.findById(typingData.getDialog());
        Optional<Person> personOptional = personRepository.findById(typingData.getAuthor());
        if (dialog.isPresent() && personOptional.isPresent()) {
            dialog.get().getPersons().forEach(person -> {
                if (person.getId() != typingData.getAuthor()) {
                    TypingResponse typingResponse = new TypingResponse();
                    typingResponse.setDialog(typingData.getDialog());
                    typingResponse.setAuthorId(typingData.getAuthor());
                    typingResponse.setAuthor(personOptional.get().getFirstName());
                    notificationService.sendEvent("start-typing-response", typingResponse, person.getId());
                }
            });
        }
    }

    public void stopTyping(TypingData typingData) {
        Optional<Dialog> dialog = dialogRepository.findById(typingData.getDialog());
        Optional<Person> personOptional = personRepository.findById(typingData.getAuthor());
        if (dialog.isPresent() && personOptional.isPresent()) {
            dialog.get().getPersons().forEach(person -> {
                if (person.getId() != typingData.getAuthor())
                    notificationService.sendEvent("stop-typing-response", typingData, person.getId());
            });
        }
    }

    public void readMessage(ReadMessagesData readMessagesData, int personId) {
        Optional<Dialog> dialog = dialogRepository.findById(readMessagesData.getDialog());
        Optional<Person> personOptional = personRepository.findById(personId);
        if (dialog.isPresent() && personOptional.isPresent()) {
            person2DialogRepository.findPerson2DialogByDialogIdAndPersonId(dialog.get().getId(), personOptional.get().getId())
                    .ifPresent(person2Dialog -> person2DialogRepository.save(person2Dialog.setLastCheckTime(LocalDateTime.now())));
            notificationService.sendEvent("unread-response", person2DialogRepository
                    .findUnreadMessagesCount(personOptional.get().getId())
                    .orElse(0).toString(), personOptional.get().getId());

        }
    }
}
