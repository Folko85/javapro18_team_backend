package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;

//@SpringBootTest(classes = {NetworkApplication.class})
public class PostServiceTest{
//    @Autowired
//    private FriendshipRepository friendshipRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    String text  = "";
//    long dateF =  0;
//    long dateT = -1;
//    int offset =  0;
//    int itemPerPage = 20;
//
//    @Test
//    public void getPosts(){
//        List<Integer> personList = friendshipRepository.findBlockersIds(1);
//        if(!personList.isEmpty()){
//            for (Integer p: personList){
//                System.out.println(p);
//            }
//        }
//        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
//        Instant dateFrom = Instant.ofEpochMilli(dateF);
//        Instant dateTo = Instant.now();
//        Page<Post> postList =  postRepository.findPostsByTextContainingByDateExcludingBlockers("", dateFrom, dateTo,"", pageable, personList);
//        if(!postList.isEmpty()){
//            for(Post p :postList.toList()){
//                System.out.println(p.getTitle());
//                System.out.println(p.getPostText());
//                System.out.println("AUTHOR:  " + p.getPerson().getFirstName() + " " + p.getPerson().getLastName());
//
//            }
//        }
//        List<Integer> personList1 = friendshipRepository.findBlockersIds(1);
//        Page<Post> postLists =  postRepository.findPostsByTextContainingByDateExcludingBlockers("", dateFrom, dateTo, "M", pageable, personList1);
//        if(!postLists.isEmpty()){
//            for(Post p :postLists.toList()){
//                System.out.println(p.getTitle());
//                System.out.println(p.getPostText());
//                System.out.println("AUTHOR:  " + p.getPerson().getFirstName() + " " + p.getPerson().getLastName());
//
//            }
//        }
//
//    }
//
//    @Test
//    public void testGetFeeds(){
//        List<Integer> personList = friendshipRepository.findBlockersIds(1);
//        if(!personList.isEmpty()){
//            for (Integer p: personList){
//                System.out.println(p);
//            }
//        }
//        offset =0;
//        itemPerPage=20;
//        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
//        Page<Post> postList =  postRepository.findPostsByTextContainingExcludingBlockers("", pageable, personList);
//        if(!postList.isEmpty()){
//            for(Post p :postList.getContent()){
//                System.out.println(p.getTitle());
//                System.out.println(p.getPostText());
//
//            }
//        }
//        List<Integer> empty = friendshipRepository.findBlockersIds(1);
//        Assertions.assertTrue(empty.isEmpty());
//        Page<Post> emptyPostList =  postRepository.findPostsByTextContainingExcludingBlockers(text, pageable, empty);
//        Assertions.assertTrue(!emptyPostList.isEmpty());
//        int i=1;
//        for(Post p :emptyPostList.toList()){
//            System.out.println(i++);
//            System.out.println(p.getTitle());
//            System.out.println(p.getPostText());
//
//        }
//    }
}
