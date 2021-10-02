package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.PostDTO.PostData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static com.skillbox.socialnetwork.service.CommentService.getCommentData4Response;
import static com.skillbox.socialnetwork.service.CommentService.getCommentWallData4Response;
import static com.skillbox.socialnetwork.service.UserServiceImpl.convertLocalDateTime;
import static java.time.ZoneOffset.UTC;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public PostService(PostRepository postRepository, AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    public PostResponse getPosts(String text, long dateFrom, long dateTo, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContainingByDate(text,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(dateFrom), UTC)
                , LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTo), UTC),
                pageable);
        System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(dateFrom), UTC));
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public PostResponse getFeeds(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContaining(text, pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    private PostResponse getPostResponse(int offset, int itemPerPage, Page<Post> pageablePostList, Person person) {
        PostResponse postResponse = new PostResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePostList.getTotalElements());
        postResponse.setData(getPost4Response(pageablePostList.toList(), person));

        return postResponse;
    }

    private List<PostData> getPost4Response(List<Post> posts, Person person) {
        List<PostData> postDataList = new ArrayList<>();
        posts.forEach(post -> {
            PostData postData = getPostData(post, person);
            postDataList.add(postData);
        });
        return postDataList;
    }

    private PostData getPostData(Post post, Person person) {
        PostData postData = new PostData();
        postData.setPostText(post.getPostText());
        postData.setAuthor(setAuthData(post.getPerson()));
        postData.setComments(getCommentData4Response(post.getComments()));
        postData.setId(post.getId());
        postData.setLikes(post.getPostLikes().size());
        postData.setTime(post.getDatetime().toInstant(UTC));
        postData.setTitle(post.getTitle());
        postData.setBlocked(post.isBlocked());
        postData.setMyLike(post.getPostLikes().stream()
                .anyMatch(postLike -> postLike.getPerson().equals(person)));
        return postData;
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }
    public List<PostWallData> getPastWallData(Integer offset, Integer itemPerPage, UserRest userRest){
        Pageable pageable = PageRequest.of(offset/itemPerPage, itemPerPage);
        Page<Post> page =postRepository.findUserPost(userRest.getId(), pageable);
        List<Post> posts =page.getContent();
        List<PostWallData> postWallDataList = new ArrayList<>();
        posts.forEach(post -> {
            PostWallData postWallData = getPostWallData(post, userRest);
            postWallDataList.add(postWallData);
        });
        return postWallDataList;
    }

    public PostWallData getPostWallData(Post post, UserRest userRest) {
        PostWallData postWallData = new PostWallData();
        postWallData.setPostText(post.getPostText());
        postWallData.setAuthor(userRest);
        postWallData.setComments(getCommentWallData4Response(post.getComments()));
        postWallData.setId(post.getId());
        postWallData.setLikes(post.getPostLikes().size());
        postWallData.setTime(convertLocalDateTime(post.getDatetime()));
        postWallData.setTitle(post.getTitle());
        postWallData.setBlocked(post.isBlocked());
        if(LocalDateTime.now().isBefore(post.getDatetime())){
            postWallData.setType("QUEUED");
        }
        else{
            postWallData.setType("POSTED");
        }
        return postWallData;
    }





}
