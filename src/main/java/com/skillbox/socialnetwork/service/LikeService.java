package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.likeDTO.LikeData;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.LikeRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeService(LikeRepository likeRepository,
                       AccountRepository accountRepository,
                       PostRepository postRepository,
                       CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public DataResponse getLikes(int itemId, String type) throws PostNotFoundException {

        return getLikesResponse(itemId, type);
    }

    public DataResponse putLikes(LikeRequest likeRequest, Principal principal) throws PostNotFoundException, LikeNotFoundException {
        Person person = findPerson(principal.getName());
        if (likeRepository.findLikeByItemAndTypeAndPerson(likeRequest.getItemId(), likeRequest.getType(), person).isPresent())
            throw new LikeNotFoundException();
        checkItemId(likeRequest.getItemId(), likeRequest.getType());
        Like like = new Like();
        like.setItem(likeRequest.getItemId());
        like.setType(likeRequest.getType());
        like.setPerson(person);
        like.setTime(LocalDateTime.now());
        likeRepository.save(like);
        return getLikesResponse(likeRequest.getItemId(), likeRequest.getType());
    }

    public DataResponse deleteLike(int itemId, String type, Principal principal) throws PostNotFoundException, LikeNotFoundException {
        Person person = findPerson(principal.getName());
        Like like = likeRepository.findLikeByItemAndTypeAndPerson(itemId, type, person)
                .orElseThrow(LikeNotFoundException::new);
        likeRepository.delete(like);
        return getLikesResponse(itemId, type);
    }

//    public DataResponse getLiked(int itemId, int userId) throws PostNotFoundException {
//       Person person = findPerson(p)
//        DataResponse likeResponse = new DataResponse();
//        LikeData likeData = new LikeData();
//        likeData.setLikes(likeRepository.findLikeByItemAndTypeAndPerson(itemId, userId)
//                .isPresent() ? "true" : "false");
//        likeResponse.setData(likeData);
//        return likeResponse;
//    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

//    private Post findPost(int itemId) throws PostNotFoundException {
//        return postRepository.findById(itemId)
//                .orElseThrow(PostNotFoundException::new);
//    }
//
//    private PostComment findPostComment(int itemId) throws CommentNotFoundException {
//        return commentRepository.findById(itemId)
//                .orElseThrow(CommentNotFoundException::new);
//    }

    private DataResponse getLikesResponse(int itemId, String type) {
        DataResponse likeResponse = new DataResponse();
        LikeData likeData = new LikeData();
        List<Integer> users = new ArrayList<>();
        likeRepository.findLikesByItemAndType(itemId, type).forEach(postLike1 -> {
            users.add(postLike1.getPerson().getId());
        });
        likeData.setLikes(String.valueOf(users.size()));
        likeData.setUsers(users);
        likeResponse.setData(likeData);
        return likeResponse;
    }

    private void checkItemId(int itemId, String type) throws LikeNotFoundException {
        if (!(type.equals("Post") && postRepository.findById(itemId).isPresent()))
            if (!(type.equals("Comment") && commentRepository.findById(itemId).isPresent()))
                throw new LikeNotFoundException();
    }


}
