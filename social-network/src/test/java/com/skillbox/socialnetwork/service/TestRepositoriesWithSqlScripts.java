package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.AbstractTestsWithSqlScripts;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
public class TestRepositoriesWithSqlScripts extends AbstractTestsWithSqlScripts {

    private String blockedEmail="kburgan4@comcast.net";
    private String[] blockersEmails = {
            "jboner0@domainmarket.com",
            "cfurby1@webeden.co.uk",
            "bjacobowits2@wsj.com",
            "jpeagram3@virginia.edu",
            "gdomerc5@unesco.org",
    };

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipForTestRepository friendshipForTestRepository;

    @Autowired
    PersonRepository personRepository;

    @Test
    @DisplayName("Получаем сущности с блокировкой пользователя с id")
    public void testGetBlocks() {
        List<Integer> blockers = new ArrayList<>();
        Arrays.stream(blockersEmails).forEach(x->blockers.add(personRepository.findByEMail(blockedEmail).get().getId()));
        Person person = personRepository.findByEMail(blockedEmail).get();

        List<String> checkingFriendShips = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
//        friendshipList.forEach(x->{
//            if(x.getSrcPerson().getEMail().equals(blockedEmail)){
//                checkingFriendShips.add(x.getDstPerson().getEMail());
//                ids.add(x.getDstPerson().getId());
//            }
//            else{
//                checkingFriendShips.add(x.getSrcPerson().getEMail());
//                ids.add(x.getSrcPerson().getId());
//            }
//
//        });
        List<Integer> checkingIds=  friendshipRepository.findBlockersIds(person.getId());
        Assertions.assertArrayEquals(blockers.toArray(),checkingIds.toArray());

    }

    @Test
    public void testGetBlocker() {
        List<Integer> personList = friendshipRepository.findBlockersIds(5);
        if (!personList.isEmpty()) {
            for (Integer p : personList) {
                System.out.println(p);
            }
        }
        List<Integer> empty = friendshipRepository.findBlockersIds(10);
        System.out.println(empty.isEmpty());
    }


}
