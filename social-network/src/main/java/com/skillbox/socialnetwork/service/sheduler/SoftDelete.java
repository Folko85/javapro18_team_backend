package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PersonService;
import com.skillbox.socialnetwork.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class SoftDelete {

     private final PostService postService;
     private final PersonService personService;
     private final CommentService commentService;

    public SoftDelete(PostService postService, PersonService personService, CommentService commentService) {
        this.postService = postService;
        this.personService = personService;
        this.commentService = commentService;
    }


    @Scheduled(cron = "@daily")
    public void cleanupDB() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime personDelete = now.minusYears(1);
        LocalDateTime postDelete = now.minusMonths(1);
        LocalDateTime commentDelete = now.minusDays(1);

        log.info("Запустили процесс удаления усстаревших данных");
        try {
//            personService.updateAfterSoft(personDelete); //остается видимым но без имени, заполняемых данных и с тене-авой. Так же messagesPermission = NOBODY.
//            postService.deleteAfterSoft(postDelete);
//            commentService.deleteAfterSoft(commentDelete);

        }catch (Exception e){
            log.error(e.getMessage());
        }
        log.info("Устаревшие данные удалены" + now);

    }
}
