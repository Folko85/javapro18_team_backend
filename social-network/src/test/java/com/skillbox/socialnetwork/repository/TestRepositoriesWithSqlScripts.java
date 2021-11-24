package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.AbstractTestsWithSqlScripts;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.FriendshipStatusRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
@Slf4j
public class TestRepositoriesWithSqlScripts extends AbstractTestsWithSqlScripts {

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FriendshipStatusRepository friendshipStatusRepository;

    //Target Person
    private static String blockedEmail = "kburgan4@comcast.net";
    //Пользователи с этими почтами заблокировали с почтой blockedEmail
    private static final String[] blockersEmails = {
            "jboner0@domainmarket.com",
            "cfurby1@webeden.co.uk",
            "bjacobowits2@wsj.com",
            "jpeagram3@virginia.edu",
            "gdomerc5@unesco.org",
    };
    //Пользователи с этими почтами имеют связь френдшип, но с неблокирующими статусами
    private static final String[] whitePersons = {
            "kmattussevich6@alibaba.com",
            "ipinar7@wisc.edu",
            "goshea8@apple.com",
            "sdeavin9@booking.com",
            "xbanaszewskia@delicious.com",
            "cbuncherb@reverbnation.com"
    };

    private static List<Integer> blockersIds = new ArrayList<>();
    private static List<Integer> whitePersonsIds = new ArrayList<>();
    private static Person target;

    @BeforeAll
    /* * * *
     * TODO
     * А может получение параметров для теста тоже сделать тестом?
     * * * */
    static void setUpData(@Autowired PersonRepository personRepository ) {

        Arrays.stream(blockersEmails).forEach(x -> {
            blockersIds.add(personRepository.findByEMail(x).get().getId());
            System.out.println(x);
        });
        Arrays.stream(whitePersons).forEach(x -> {
            whitePersonsIds.add(personRepository.findByEMail(x).get().getId());
            System.out.println(x);
        });

        target = personRepository.findByEMail(blockedEmail).get();
    }

    @Test
    @DisplayName("Проверяем список id пользователей, которые заблокировали пользователя с почтой blockedEmail")
    public void testFindBlockersIds() {
        log.info("Target id: {}, Email: {}", target.getId(), target.getEMail());
        List<Integer> checkingIds = friendshipRepository.findBlockersIds(target.getId());
        Assertions.assertArrayEquals(blockersIds.toArray(), checkingIds.toArray(),"Массивы с id не равны");
        HashSet<Integer> ids = new HashSet<>(checkingIds);
        whitePersonsIds.forEach(x -> {
            if (ids.contains(x)) {
                Optional<Friendship> friendsh = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(target.getId(), x);
                Assertions.assertFalse(friendsh.isEmpty(),"Получили Пустой Френдиш");
                Assertions.fail("В списке блокирующих был найден недопустимый id ");
                Friendship friendship = friendsh.get();
                log.error("FriendShip: Src id and email : {} , {} # status: {}  # Dst id and email : {} , {} ",
                        friendship.getSrcPerson().getId(), friendship.getSrcPerson().getEMail(), friendship.getStatus(),
                        friendship.getDstPerson().getId(), friendship.getDstPerson().getEMail()
                );
            }
        });
        //Этого тестового пользователя никто не блокировал
        Person person = personRepository.findByEMail("jboner0@domainmarket.com").get();
        Assertions.assertTrue(friendshipRepository.findBlockersIds(person.getId()).isEmpty());

    }


    String text  = "";
    long dateF =  0;
    long dateT = -1;
    int offset =  0;
    int itemPerPage = 20;

    @Test
    @DisplayName("Проверяем получение постов")
    public void getPosts(){
        log.info("Проверяем получение постов для пользователя без блокировок");

        Person person = personRepository.findByEMail("jboner0@domainmarket.com").get();
        List<Integer> personList = friendshipRepository.findBlockersIds(person.getId());
        personList.add(-1);
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Instant dateFrom = Instant.ofEpochMilli(dateF);
        Instant dateTo = Instant.now();
        Page<Post> postList =  postRepository.findPostsByTextContainingByDateExcludingBlockers("","", dateFrom, dateTo, pageable, personList);
        Assertions.assertFalse(postList.isEmpty(), "Получили пустой список постов");
        Assertions.assertEquals(postList.get().count(), itemPerPage, "Получили постов меньше, чем дефолтное значение 20");
        Page<Post> postListM =  postRepository.findPostsByTextContainingByDateExcludingBlockers("","M", dateFrom, dateTo, pageable, personList);
        Assertions.assertTrue(postListM.isEmpty(), "Получили не пустой список постов с автором M");
        Page<Post> postListsG =  postRepository.findPostsByTextContainingByDateExcludingBlockers("","G", dateFrom, dateTo, pageable, personList);
        Assertions.assertEquals(4L, postListsG.get().count(), "Количество постов автора G не равно 4");


        log.info("Проверяем получение постов для пользователя Target");
        HashSet<Integer> blockersHashSet = new HashSet<>(blockersIds);

        Pageable pageable2 = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> postList2 =  postRepository.findPostsByTextContainingByDateExcludingBlockers("","", dateFrom, dateTo, pageable, blockersIds);
        Assertions.assertFalse(postList2.isEmpty(), "Получили пустой список постов для Target");
        AtomicInteger countOfTargetPosts = new AtomicInteger();
        postList2.toList().forEach(post -> {
            Assertions.assertFalse(blockersHashSet.contains(post.getPerson().getId()),"Список постов содержит пост блокера Target");
            if(post.getPerson().getId().equals(target.getId())){
                countOfTargetPosts.getAndIncrement();
            }
        });
        Assertions.assertEquals(2, countOfTargetPosts.get(), "Не получили два поста Target");


    }


}
