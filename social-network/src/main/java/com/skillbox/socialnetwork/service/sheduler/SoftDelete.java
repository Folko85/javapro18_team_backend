package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import com.skillbox.socialnetwork.service.AccountService;
import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PersonService;
import com.skillbox.socialnetwork.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class SoftDelete {

    private final PostService postService;
    private final AccountService accountService;
    private final CommentService commentService;
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    LocalDateTime now = LocalDateTime.now();

    public SoftDelete(PostService postService, AccountService personService, CommentService commentService, PersonRepository personRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.postService = postService;
        this.accountService = personService;
        this.commentService = commentService;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }


    @Scheduled(cron = "@daily")
    public void cleanupPerson() {

        log.info("Запустили процесс удаления усстаревших аккаунтов");
        try {
            List<Person> persons = personRepository.findSoftDeletedPersonsID(now.minusMonths(3));
            for (Person person : persons){
                accountService.updateAfterSoftDelete(person);
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
                postService.deleteAfterSoft(post);
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
