package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.likedto.LikeData;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.LikeRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
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
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeService(LikeRepository likeRepository,
                       PersonRepository personRepository,
                       PostRepository postRepository,
                       CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public DataResponse<LikeData> getLikes(int itemId, String type) {

        return getLikesResponse(itemId, type);
    }

    public DataResponse<LikeData> putLikes(LikeRequest likeRequest, Principal principal) throws LikeNotFoundException {
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

    public DataResponse<LikeData> deleteLike(int itemId, String type, Principal principal) throws LikeNotFoundException {
        Person person = findPerson(principal.getName());
        Like like = likeRepository.findLikeByItemAndTypeAndPerson(itemId, type, person)
                .orElseThrow(LikeNotFoundException::new);
        likeRepository.delete(like);
        return getLikesResponse(itemId, type);
    }


    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }


    private DataResponse<LikeData> getLikesResponse(int itemId, String type) {
        DataResponse<LikeData> likeResponse = new DataResponse<>();
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
