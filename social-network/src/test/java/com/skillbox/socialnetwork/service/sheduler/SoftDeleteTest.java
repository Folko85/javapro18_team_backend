package com.skillbox.socialnetwork.service.sheduler;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.entity.PostFile;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.FileRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import com.skillbox.socialnetwork.service.CommentService;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
class SoftDeleteTest extends AbstractTest {


    @Test
    public void testScheduler() {
        org.springframework.scheduling.support.CronTrigger trigger =
                new CronTrigger("0 0 0 * * *"); //каждый день в 00:00:00
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE");
        final Date yesterday = today.getTime();
        System.out.println("Yesterday was : " + df.format(yesterday));
        Date nextExecutionTime = trigger.nextExecutionTime(
                new TriggerContext() {

                    @Override
                    public Date lastScheduledExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastActualExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastCompletionTime() {
                        return yesterday;
                    }
                });

        String message = "Next Execution date: " + df.format(nextExecutionTime);
        System.out.println(message);
    }

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;


    private Person person;
    private Post post;
    private PostComment postComment;
    private PostFile postFile;
    private PostFile commentFile;


    @BeforeEach
    public void setup() {
        super.setup();
        person = new Person();
        person.setPassword("password");
        person.setFirstName("Valera");
        person.setLastName("Jma");
        person.setEMail("test@test.ru");
        person.setPassword("password");
        person.setBirthday(LocalDate.of(1988, 1, 5));
        person.setRole(Role.USER);
        person.setApproved(true);
        person.setDeleted(true);
        person.setDeletedTimestamp(LocalDateTime.now().minusMonths(4));
        person.setDateAndTimeOfRegistration(LocalDateTime.now().minusMonths(9));
        person.setBlocked(false);
        personRepository.save(person);

        post = new Post();
        post.setPerson(person);
        post.setPostText("ABOBA");
        post.setDeleted(true);
        post.setDeletedTimestamp(LocalDateTime.now().minusMonths(1));
        postRepository.save(post);

        postComment = new PostComment();
        postComment.setPerson(person);
        postComment.setCommentText("ABOBA");
        postComment.setTime(LocalDateTime.now().minusMonths(5));
        postComment.setDeleted(true);
        postComment.setDeletedTimestamp(LocalDateTime.now().minusMonths(1));
        postComment.setPost(post);
        commentRepository.save(postComment);

        postFile = new PostFile();
        postFile.setPostId(post.getId());
        postFile.setUserId(person.getId());

        commentFile = new PostFile();
        commentFile.setCommentId(postComment.getId());
        commentFile.setUserId(person.getId());

        fileRepository.save(postFile);
        fileRepository.save(commentFile);
    }


    @AfterEach
    public void cleanup() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        personRepository.deleteAll();
        fileRepository.deleteAll();
    }

    @Test
    void cleanupPerson() {

        List<Person> personList = personRepository.findSoftDeletedPersonsID(LocalDateTime.now().minusMonths(1));
        userService.updateAfterSoftDelete(person);

        Assertions.assertNotEquals(personList.get(0), person);
        Assertions.assertEquals(person.getFirstName(), "Deleted");
        Assertions.assertEquals(person.getLastName(), "Deleted");
        Assertions.assertNull(person.getPhone());
        Assertions.assertNull(person.getCity());

    }

    @Test
    void cleanupPost() {
        postService.deletePostAfterSoft(post);

        Assertions.assertEquals(post.getPostText(), "Deleted");
        Assertions.assertEquals(post.getTitle(), "Deleted");
        Assertions.assertFalse(post.isDeleted());
        Assertions.assertNotNull(postFile);


    }

    @Test
    void cleanupPostComment() {
        List<PostComment> postCommentList = commentRepository.findSoftDeletedCommentsID(LocalDateTime.now().minusDays(8));
        Assertions.assertEquals(postCommentList.get(0).getId(),postComment.getId());
        commentService.deleteAfterSoft(postComment);

        Assertions.assertEquals(postComment.getCommentText(), "Deleted");
        Assertions.assertFalse(postComment.isDeleted());

    }
}