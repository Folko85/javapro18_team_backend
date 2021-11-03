package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.сommentdto.CommentData;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.LikeRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static com.skillbox.socialnetwork.service.AuthService.setBlockerAuthData;
import static com.skillbox.socialnetwork.service.AuthService.setDeletedAuthData;
import static java.time.ZoneOffset.UTC;

@Service
public class CommentService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FriendshipService friendshipService;

    public CommentService(PersonRepository personRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository, LikeRepository likeRepository, FriendshipService friendshipService) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.friendshipService = friendshipService;
    }

    public ListResponse<CommentData> getPostComments(int offset, int itemPerPage, int id, Principal principal) throws PostNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = findPost(id);
        return getPage4PostComments(offset, itemPerPage, post, person);
    }

    public ListResponse<CommentData> getPage4PostComments(int offset, int itemPerPage, Post post, Person person) {
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<PostComment> pageablePostCommentList = commentRepository
                .findPostCommentsByPostIdAndParentIsNull(post.getId(), pageable);
        return getPostResponse(offset, itemPerPage, pageablePostCommentList, person);
    }

    public DataResponse<CommentData> postComment(int itemId, CommentRequest commentRequest, Principal principal) throws PostNotFoundException, CommentNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = findPost(itemId);
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
        return getCommentResponse(postComment, person);
    }

    public DataResponse<CommentData> putComment(int itemId, int commentId, CommentRequest commentRequest, Principal principal) throws CommentNotFoundException, PostNotFoundException {
        Person person = findPerson(principal.getName());
        findPost(itemId);
        if (commentRequest.getParentId() != null)
            findPostComment(commentRequest.getParentId());
        PostComment postComment = findPostComment(commentId);
        postComment.setCommentText(commentRequest.getCommentText());
        commentRepository.save(postComment);
        return getCommentResponse(postComment, person);
    }

    public DataResponse<CommentData> deleteComment(int itemId, int commentId, Principal principal) throws CommentNotFoundException {
        Person person = findPerson(principal.getName());
        PostComment postComment = findPostComment(commentId);
        postComment.setDeleted(postComment.getPerson().getId() == person.getId() || postComment.isDeleted());
        commentRepository.save(postComment);
        return getCommentResponse(postComment, person);
    }

    public DataResponse<CommentData> recoveryComment(int itemId, int commentId, Principal principal) throws CommentNotFoundException {
        Person person = findPerson(principal.getName());
        PostComment postComment = findPostComment(commentId);
        postComment.setDeleted(postComment.getPerson().getId() != person.getId() && postComment.isDeleted());
        commentRepository.save(postComment);
        return getCommentResponse(postComment, person);
    }

    public List<CommentData> getCommentData4Response(Set<PostComment> comments, Person person) {
        List<CommentData> commentDataList = new ArrayList<>();
        comments.forEach(postComment -> {
            CommentData commentData = getCommentData(postComment, person);
            postComment.getPostComments()
                    .forEach(comment -> commentData.getSubComments().add(getCommentData(comment, person)));
            commentDataList.add(commentData);
        });
        return new ArrayList<>(commentDataList);
    }

    public CommentData getCommentData(PostComment postComment, Person person) {
        CommentData commentData = new CommentData();
        commentData.setCommentText(postComment.getCommentText());
        commentData.setBlocked(postComment.isBlocked());
        if(postComment.getPerson().isDeleted()){
            commentData.setAuthor(setDeletedAuthData(postComment.getPerson()));
        }
        else if(postComment.getPerson().getId().equals(person.getId()) || !friendshipService.isBlockedBy(postComment.getPerson().getId(), person.getId())) {
            commentData.setAuthor(setAuthData(postComment.getPerson()));
        }
        else{
            commentData.setAuthor(setBlockerAuthData(postComment.getPerson()));
        }
        commentData.setId(postComment.getId());
        commentData.setTime(postComment.getTime().toInstant(UTC));
        commentData.setDeleted(postComment.isDeleted());
        Set<Like> likes = likeRepository.findLikesByItemAndType(postComment.getId(),"Comment");
        commentData.setLikes(likes.size());
        commentData.setMyLike(likes.stream()
                .anyMatch(commentLike -> commentLike.getPerson().equals(person)));
        if (postComment.getParent() != null)
            commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        commentData.setSubComments(new ArrayList<>());
        return commentData;
    }

    private ListResponse<CommentData> getPostResponse(int offset, int itemPerPage, Page<PostComment> pageablePostCommentList, Person person) {
        ListResponse<CommentData> postCommentResponse = new ListResponse<>();
        postCommentResponse.setPerPage(itemPerPage);
        postCommentResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postCommentResponse.setOffset(offset);
        postCommentResponse.setTotal((int) pageablePostCommentList.getTotalElements());
        postCommentResponse.setData(getCommentData4Response(pageablePostCommentList.toSet(), person));
        return postCommentResponse;
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private Post findPost(int itemId) throws PostNotFoundException {
        return postRepository.findById(itemId)
                .orElseThrow(PostNotFoundException::new);
    }

    private PostComment findPostComment(int itemId) throws CommentNotFoundException {
        return commentRepository.findById(itemId)
                .orElseThrow(CommentNotFoundException::new);
    }

    public DataResponse<CommentData> getCommentResponse(PostComment postComment, Person person) {
        DataResponse<CommentData> commentResponse = new DataResponse<>();
        commentResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        commentResponse.setData(getCommentData(postComment, person));
        return commentResponse;
    }
}
