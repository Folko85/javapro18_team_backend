package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest(classes = {NetworkApplication.class})
public class PersonServiceTest{
//
//    @Autowired
//    PersonRepository personRepository;
//
//    @Autowired
//    FriendshipRepository friendshipRepository;
//
//    @Autowired
//    FriendshipService friendshipService;
//
//
//    String firstName;
//    String lastName;
//    int ageF;
//    int ageT;
//    String city;
//    String country;
//    int offset;
//    int itemPerPage;
//
//    public PersonServiceTest() {
//        this.firstName = "";
//        this.lastName = "";
//        this.ageF = 1;
//        this.ageT = 120;
//        this.city ="";
//        this.country = "";
//        this.offset = 0;
//        this.itemPerPage = 20;
//    }
//
//    @Test
//    public void getAllPersonsWithDefaultParametrs() {
//        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
//        LocalDate ageFrom = LocalDate.now().minusYears(ageT);
//        LocalDate ageTo = LocalDate.now().minusYears(ageF);
//        List<Integer> blockers = friendshipService.getBlockersId(1);
//        System.out.println(blockers.size());
//        Page<Person> personList = personRepository.findByOptionalParametrs("M", "", ageFrom, ageTo, city, country, pageable, blockers);
//        if(!personList.isEmpty()){
//            for(Person p: personList.getContent()){
//                System.out.println(p.getFirstName());
//            }
//        }
//
//    }


}