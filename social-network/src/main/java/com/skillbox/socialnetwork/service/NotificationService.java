package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.entity.Notification;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.NotificationRepository;
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
import java.util.List;

import static java.time.ZoneOffset.UTC;
@Service
public class NotificationService {
    private final PersonRepository personRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(PersonRepository personRepository, NotificationRepository notificationRepository) {
        this.personRepository = personRepository;
        this.notificationRepository = notificationRepository;
    }

    public ListResponse<NotificationData> getNotification(int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Notification> notificationPage = notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId(), pageable);
        return getNotificationResponse(offset, itemPerPage, notificationPage);
    }

    private ListResponse<NotificationData> getNotificationResponse(int offset, int itemPerPage, Page<Notification> notificationPage) {
        ListResponse<NotificationData> postResponse = new ListResponse<>();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) notificationPage.getTotalElements());
        postResponse.setData(getNotification4Response(notificationPage.toList()));
        return postResponse;
    }

    private List<NotificationData> getNotification4Response(List<Notification> notifications) {
        List<NotificationData> notificationDataList = new ArrayList<>();
        notifications.forEach(notification -> {
            NotificationData notificationData = getNotificationData(notification);
            notificationDataList.add(notificationData);
        });
        return notificationDataList;
    }

    private NotificationData getNotificationData(Notification notification) {
        NotificationData notificationData = new NotificationData();
        notificationData.setId(notification.getId());
        notificationData.setEntityId(notification.getEntityId());
        notificationData.setSentTime(Instant.now());
        notificationData.setInfo("poka tak");
        notificationData.setSentTime(notification.getSendTime().toInstant(UTC));
        notificationData.setTypeId(1);
        return notificationData;
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }
}
