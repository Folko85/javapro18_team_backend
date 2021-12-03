package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.repository.*;
import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.StorageService;
import com.skillbox.socialnetwork.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
public class SoftDelete {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final StorageService storageService;
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final FileRepository fileRepository;

    @Value("${soft.person.month}")
    private int cleanupPersonMonths;

    @Value("${soft.post.day}")
    private int cleanupPostDays;

    @Value("${soft.comment.day}")
    private int cleanupCommentDays;


    public SoftDelete(PostService postService, UserService personService, CommentService commentService, StorageService storageService, PersonRepository personRepository, PostRepository postRepository, CommentRepository commentRepository, NotificationRepository notificationRepository, FileRepository fileRepository) {
        this.postService = postService;
        this.userService = personService;
        this.commentService = commentService;
        this.storageService = storageService;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
        this.fileRepository = fileRepository;
    }


    @Scheduled(cron = "@daily")
    public void cleanupPerson() {
        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.info("Запустили процесс удаления усстаревших аккаунтов");
        try {

            List<Person> persons = personRepository.findSoftDeletedPersonsID(now.minusMonths(cleanupPersonMonths));
            for (Person person : persons) {
                userService.updateAfterSoftDelete(person); //меняем данные
                List<Notification> notificationList =
                        notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId());
                assert notificationList != null;
                notificationRepository.deleteAll(notificationList); //удаляем уведомления
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие аккаунты удалены" + now);
    }

    @Scheduled(cron = "@daily")
    public void cleanupPost() {
        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        log.info("Запустили процесс удаления усстаревших постов");
        try {
            List<Post> posts = postRepository.findSoftDeletedPostsID(now.minusDays(cleanupPostDays));
            for (Post post : posts) {
                postService.deletePostAfterSoft(post);
                PostFile postFile = fileRepository.findByPostId(post.getId());
                assert postFile != null;
                storageService.deleteImage(postFile.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие посты удалены" + now);
    }

    @Scheduled(cron = "@daily")
    public void cleanupPostComment() {
        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        log.info("Запустили процесс удаления усстаревших комментариев");
        try {
            List<PostComment> postComments = commentRepository.findSoftDeletedCommentsID(now.minusDays(cleanupCommentDays));
            for (PostComment postComment : postComments) {
                commentService.deleteAfterSoft(postComment);
                PostFile commentFile = fileRepository.findByCommentId(postComment.getId());
                assert commentFile != null;
                storageService.deleteImage(commentFile.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие комментарии удалены" + now);
    }
}
