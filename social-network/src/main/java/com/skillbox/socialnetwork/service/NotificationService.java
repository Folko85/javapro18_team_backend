package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.entity.Notification;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import com.skillbox.socialnetwork.repository.*;
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
import java.util.Optional;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Service
public class NotificationService {
    private final CommentRepository commentRepository;
    private final FriendshipRepository friendshipRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(CommentRepository commentRepository,
                               FriendshipRepository friendshipRepository,
                               MessageRepository messageRepository,
                               PersonRepository personRepository,
                               NotificationRepository notificationRepository) {
        this.commentRepository = commentRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
        this.notificationRepository = notificationRepository;
    }

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
            if(notificationOptional.isPresent())
            {
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
            if (notificationData.getEntityAuthor() != null)
                notificationDataList.add(notificationData);
        });
        return notificationDataList;
    }

    private NotificationData getNotificationData(Notification notification) {
        NotificationData notificationData = new NotificationData();
        notificationData.setId(notification.getId());
        notificationData.setSentTime(Instant.now());
        notificationData.setInfo("poka tak");
        notificationData.setSentTime(notification.getSendTime().toInstant(UTC));
        notificationData.setEventType(notification.getType());
        switch (notification.getType()) {
            case COMMENT_COMMENT, POST_COMMENT -> {
                notificationData.setEntityAuthor(commentRepository.findById(notification.getEntityId())
                        .map(postComment -> setAuthData(postComment.getPerson())).orElse(null));
                notificationData.setEntityId(notificationData.getEntityAuthor().getId());
            }
            case FRIEND_REQUEST -> {
                notificationData.setEntityAuthor(friendshipRepository.findById(notification.getEntityId())
                        .map(friendship -> setAuthData(friendship.getSrcPerson())).orElse(null));
                notificationData.setEntityId(notificationData.getEntityAuthor().getId());
            }
            case MESSAGE -> {
                notificationData.setEntityAuthor(messageRepository.findById(notification.getEntityId())
                        .map(message -> setAuthData(message.getAuthor())).orElse(null));
                notificationData.setEntityId(notification.getEntityId());
            }
        }
        return notificationData;
    }


    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    public void createNotification(Person person, int entityId, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setPerson(person);
        notification.setType(notificationType);
        notification.setSendTime(LocalDateTime.now());
        notification.setEntityId(entityId);
        notificationRepository.save(notification);
    }
}
