package com.skillbox.socialnetwork.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.entity.Notification;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.MessageRepository;
import com.skillbox.socialnetwork.repository.NotificationRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationService {
    private final CommentRepository commentRepository;
    private final FriendshipRepository friendshipRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;
    private final NotificationRepository notificationRepository;
    private final SocketIOServer server;
    private final SessionRepository sessionRepository;

    public ListResponse<NotificationData> getNotification(int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Notification> notificationPage = notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId(), pageable);
        return getNotificationResponse(offset, itemPerPage, notificationPage);
    }

    public ListResponse<NotificationData> putNotification(int offset, int itemPerPage, Principal principal, int id, boolean all) {
        Person person = findPerson(principal.getName());
        if (all) {
            List<Notification> notifications = notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId());
            notifications.forEach(notification -> notification.setReadStatus(true));
            notificationRepository.saveAll(notifications);
        } else {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                Notification notification = notificationOptional.get();
                notification.setReadStatus(true);
                notificationRepository.save(notification);
            }
        }
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
        notificationData.setId(notification.getId())
                .setSentTime(notification.getSendTime().toInstant(UTC))
                .setEventType(notification.getType());
        switch (notification.getType()) {
            case COMMENT_COMMENT, POST_COMMENT -> commentRepository.findById(notification.getEntityId())
                    .ifPresent(comment -> notificationData.setEntityAuthor(setAuthData(comment.getPerson()))
                            .setEntityId(comment.getPost().getId())
                            .setParentEntityId(comment.getParent() == null ? comment.getId() : comment.getParent().getId())
                            .setCurrentEntityId(comment.getId()));
            case FRIEND_REQUEST -> friendshipRepository.findById(notification.getEntityId())
                    .ifPresent(friendship -> notificationData.setEntityAuthor(setAuthData(friendship.getSrcPerson()))
                            .setEntityId(notificationData.getEntityAuthor().getId()));
        }
        return notificationData;
    }


    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    public Notification createNotification(Person person, int entityId, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setPerson(person);
        notification.setType(notificationType);
        notification.setSendTime(LocalDateTime.now());
        notification.setEntityId(entityId);
        return notificationRepository.save(notification);
    }

    public void sendEvent(String eventName, DataResponse<MessageData> data, int personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid ->
                {
                    SocketIOClient client = server.getClient(uuid);
                    if (checkClient(client, uuid)) {
                        client.sendEvent(eventName, data);
                    }
                }
        );
        log.info("send event {} to {}", eventName, personId);
    }

    public void sendEvent(String eventName, Dto data, int personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid ->
                {
                    SocketIOClient client = server.getClient(uuid);
                    if (checkClient(client, uuid)) {
                        client.sendEvent(eventName, data);
                    }
                }
        );
        log.info("send event {} to {}", eventName, personId);
    }

    public void sendEvent(String eventName, String data, int personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid ->
                {
                    SocketIOClient client = server.getClient(uuid);
                    if (checkClient(client, uuid)) {
                        client.sendEvent(eventName, data);
                    }
                }
        );
        log.info("send event {} to {}", eventName, personId);
    }

    private boolean checkClient(SocketIOClient client, UUID uuid) {
        if (client == null) {
            sessionRepository.findByUserUUID(uuid).ifPresent(sessionRepository::deleteByUserId);
            return false;
        } else return true;
    }
}
