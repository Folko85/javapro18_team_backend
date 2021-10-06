package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.LikeDTO.LikeData;
import com.skillbox.socialnetwork.api.response.LikeDTO.LikeResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostLike;
import com.skillbox.socialnetwork.exception.PostLikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PostLikeRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public LikeService(PostLikeRepository postLikeRepository,
                       AccountRepository accountRepository,
                       PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }

    public LikeResponse getLikes(int itemId) throws PostNotFoundException {
        Post post = postRepository.findById(itemId).orElseThrow(PostNotFoundException::new);
        return getPostLikes(post);
    }

    public LikeResponse putLikes(LikeRequest likeRequest, Principal principal) throws PostNotFoundException, PostLikeNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = findPost(likeRequest.getItemId());
        if (postLikeRepository.findPostLikeByItemAndPerson(post.getId(), person.getId()).isPresent())
            throw new PostLikeNotFoundException();
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setPerson(person);
        postLike.setTime(LocalDateTime.now());
        postLikeRepository.save(postLike);
        return getLikes(likeRequest.getItemId());
    }

    public LikeResponse deleteLike(int itemId, Principal principal) throws PostNotFoundException, PostLikeNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = findPost(itemId);
        PostLike postLike = postLikeRepository.findPostLikeByItemAndPerson(post.getId(), person.getId())
                .orElseThrow(PostLikeNotFoundException::new);
        postLikeRepository.delete(postLike);
        return getPostLikes(post);
    }

    public LikeResponse getLiked(int itemId, int userId) throws PostNotFoundException {
        accountRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(""));
        postRepository.findById(itemId).orElseThrow(PostNotFoundException::new);
        LikeResponse likeResponse = new LikeResponse();
        LikeData likeData = new LikeData();
        likeData.setLikes(postLikeRepository.findPostLikeByItemAndPerson(itemId, userId)
                .isPresent() ? "true" : "false");
        likeResponse.setData(likeData);
        return likeResponse;
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private Post findPost(int itemId) throws PostNotFoundException {
        return postRepository.findById(itemId)
                .orElseThrow(PostNotFoundException::new);
    }

    private LikeResponse getPostLikes(Post post) {
        LikeResponse likeResponse = new LikeResponse();
        LikeData likeData = new LikeData();
        List<Integer> users = new ArrayList<>();
        postLikeRepository.findPostLikeByItem(post.getId()).forEach(postLike1 -> {
            users.add(postLike1.getPerson().getId());
        });
        likeData.setLikes(String.valueOf(users.size()));
        likeData.setUsers(users);
        likeResponse.setData(likeData);
        return likeResponse;
    }
}