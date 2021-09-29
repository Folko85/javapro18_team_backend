package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.CommentDTO.CommentData;
import com.skillbox.socialnetwork.api.response.CommentDTO.CommentResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.ZoneOffset.UTC;

@Service
public class CommentService {
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(AccountRepository accountRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository) {
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public CommentResponse postComment(int id, CommentRequest commentRequest, Principal principal) throws PostNotFoundException, CommentNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = findPost(id);
        PostComment postComment = new PostComment();
        if (commentRequest.getParentId() != null) {
            PostComment parentPostComment = commentRepository
                    .findById(commentRequest.getParentId()).orElseThrow(CommentNotFoundException::new);
            postComment.setParent(parentPostComment);
        }
        postComment.setCommentText(commentRequest.getCommentText());
        postComment.setPost(post);
        postComment.setTime(LocalDateTime.now());
        postComment.setPerson(person);
        postComment = commentRepository.save(postComment);
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        commentResponse.setCommentData(getCommentData(postComment));
        return commentResponse;
    }

    public static List<CommentData> getCommentData4Response(Set<PostComment> comments) {
        List<CommentData> commentDataList = new ArrayList<>();
        comments.forEach(postComment -> {
            CommentData commentData = getCommentData(postComment);
            if (commentData.getParentId() != null)
                commentDataList.stream()
                        .filter(comment -> comment.getId() == commentData.getParentId())
                        .forEach(comment -> comment.getSubComments().add(commentData));
            else commentDataList.add(commentData);
        });
        return commentDataList;
    }

    public static CommentData getCommentData(PostComment postComment) {
        CommentData commentData = new CommentData();
        commentData.setCommentText(postComment.getCommentText());
        commentData.setBlocked(postComment.isBlocked());
        commentData.setAuthorId(postComment.getPerson().getId());
        commentData.setId(postComment.getId());
        commentData.setTime(postComment.getTime().toInstant(UTC));
        if (postComment.getParent() != null)
            commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        commentData.setSubComments(new ArrayList<>());
        return commentData;
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private Post findPost(int itemId) throws PostNotFoundException {
        return postRepository.findById(itemId)
                .orElseThrow(PostNotFoundException::new);
    }
}
