package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.repository.*;
import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.StorageService;
import com.skillbox.socialnetwork.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    private LocalDateTime currentDate;

    @Value("${soft.person.month}")
    private Integer cleanupPersonMonths;

    @Value("${soft.post.day}")
    private Integer cleanupPostDays;

    @Value("${soft.comment.day}")
    private Integer cleanupCommentDays;


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

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupPerson() {
        currentDate = LocalDateTime.now();
        log.info("Запустили процесс удаления усстаревших аккаунтов");
        try {
            List<Person> persons = personRepository.findSoftDeletedPersonsID(currentDate.minusMonths(cleanupPersonMonths));
            if (CollectionUtils.isNotEmpty(persons)) {
                for (Person person : persons) {
                    userService.updateAfterSoftDelete(person); //меняем данные
                    List<Notification> notificationList =
                            notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId());
                    assert notificationList != null;
                    notificationRepository.deleteAll(notificationList); //удаляем уведомления
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие аккаунты удалены {}", currentDate);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupPost() {
        currentDate = LocalDateTime.now();
        log.info("Запустили процесс удаления усстаревших постов");
        try {
            List<Post> posts = postRepository.findSoftDeletedPostsID(currentDate.minusDays(cleanupPostDays));
            if (CollectionUtils.isNotEmpty(posts)) {
                for (Post post : posts) {
                    postService.deletePostAfterSoft(post);
                    List<PostFile> postFiles = fileRepository.findByPostId(post.getId());
                    if (CollectionUtils.isNotEmpty(postFiles)) {
                        for (PostFile postFile : postFiles) {
                            storageService.deleteImage(postFile.getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие посты удалены {}", currentDate);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupPostComment() {
        currentDate = LocalDateTime.now();
        log.info("Запустили процесс удаления усстаревших комментариев");
        try {
            List<PostComment> postComments = commentRepository.findSoftDeletedCommentsID(currentDate.minusDays(cleanupCommentDays));
            if (CollectionUtils.isNotEmpty(postComments)) {
                for (PostComment postComment : postComments) {
                    commentService.deleteAfterSoft(postComment);
                    List<PostFile> commentFiles = fileRepository.findByCommentId(postComment.getId());
                    if (CollectionUtils.isNotEmpty(commentFiles)) {
                        for (PostFile postFile : commentFiles) {
                            storageService.deleteImage(postFile.getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие комментарии удалены {}", currentDate);
    }
}
