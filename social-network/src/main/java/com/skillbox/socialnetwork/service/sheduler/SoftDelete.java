package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.repository.*;
import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final FileRepository fileRepository;

    LocalDateTime now = LocalDateTime.now();

    public SoftDelete(PostService postService, UserService personService, CommentService commentService, PersonRepository personRepository, PostRepository postRepository, CommentRepository commentRepository, NotificationRepository notificationRepository, FileRepository fileRepository) {
        this.postService = postService;
        this.userService = personService;
        this.commentService = commentService;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
        this.fileRepository = fileRepository;
    }


    @Scheduled(cron = "@daily")
    public void cleanupPerson() {

        log.info("Запустили процесс удаления усстаревших аккаунтов");
        try {
            List<Person> persons = personRepository.findSoftDeletedPersonsID(now.minusMonths(3));
            for (Person person : persons){
                userService.updateAfterSoftDelete(person); //меняем данные
                List<Notification> notificationList =
                        notificationRepository.findByPersonIdAndReadStatusIsFalse(person.getId());
                notificationRepository.deleteAll(notificationList); //удаляем уведомления
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие аккаунты удалены" + now);

    }

    @Scheduled(cron = "@daily")
    public void cleanupPost() {

        log.info("Запустили процесс удаления усстаревших постов");
        try {
            List<Post> posts = postRepository.findSoftDeletedPostsID(now.minusDays(7));
            for (Post post : posts){
                postService.deletePostAfterSoft(post);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие посты удалены" + now);

    }

    @Scheduled(cron = "@daily")
    public void cleanupPostComment() {

        log.info("Запустили процесс удаления усстаревших комментариев");
        try {
            List<PostComment> postComments = commentRepository.findSoftDeletedCommentsID(now.minusDays(1));
            for (PostComment postComment : postComments){
                commentService.deleteAfterSoft(postComment);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Устаревшие комментарии удалены" + now);

    }

}
