package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class SoftDelete {

     private final PostRepository postRepository;
     private final PersonRepository personRepository;
     private final CommentRepository commentRepository;

    public SoftDelete(PostRepository postRepository, PersonRepository personRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.personRepository = personRepository;
        this.commentRepository = commentRepository;
    }


    @Scheduled(cron = "@daily")
    public void cleanupDB() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime personDelete = now.minusYears(1);
        LocalDateTime postDelete = now.minusMonths(1);
        LocalDateTime commentDelete = now.minusDays(1);

        log.info("Запустили процесс удаления усстаревших данных");
        try {
            personRepository.deleteAfterSoft(personDelete);
            postRepository.deleteAfterSoft(postDelete);
            commentRepository.deleteAfterSoft(commentDelete);

        }catch (Exception e){
            log.error(e.getMessage());
        }
        log.info("Устаревшие данные удалены" + now);

    }
}
