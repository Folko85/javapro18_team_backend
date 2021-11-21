package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.commentdto.CommentData;
import com.skillbox.socialnetwork.api.response.platformdto.ImageDto;
import com.skillbox.socialnetwork.api.response.socketio.AuthorData;
import com.skillbox.socialnetwork.api.response.socketio.SocketNotificationData;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.socialnetwork.service.AuthService.*;
import static java.time.ZoneOffset.UTC;

@Service
public class CommentService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FriendshipService friendshipService;
    private final FileRepository fileRepository;
    private final NotificationService notificationService;

    public CommentService(PersonRepository personRepository, PostRepository postRepository,
                          CommentRepository commentRepository, LikeRepository likeRepository,
                          FriendshipService friendshipService, FileRepository fileRepository,
                          NotificationService notificationService) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.friendshipService = friendshipService;
        this.fileRepository = fileRepository;
        this.notificationService = notificationService;
    }

    public ListResponse<CommentData> getPostComments(int offset, int itemPerPage, int id, Principal principal) throws PostNotFoundException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
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
        Post post = postRepository.findById(itemId).orElseThrow(PostNotFoundException::new);
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
        if (commentRequest.getImages() != null) {
            int id = postComment.getId();
            commentRequest.getImages().forEach(image -> fileRepository.findById(Integer.parseInt(image.getId())).ifPresent(file -> fileRepository.save(file.setPostId(id))));
        }

        sendNotification(postComment);
        return getCommentResponse(postComment, person);
    }

    public DataResponse<CommentData> putComment(int itemId, int commentId, CommentRequest commentRequest, Principal principal) throws CommentNotFoundException, PostNotFoundException {
        Person person = findPerson(principal.getName());
        postRepository.findById(itemId).orElseThrow(PostNotFoundException::new);
        if (commentRequest.getParentId() != null)
            findPostComment(commentRequest.getParentId());
        PostComment postComment = findPostComment(commentId);
        postComment.setCommentText(commentRequest.getCommentText());
        commentRepository.save(postComment);
        if (commentRequest.getImages() != null) {
            int id = postComment.getId();
            commentRequest.getImages().forEach(image -> fileRepository.findById(Integer.parseInt(image.getId())).ifPresent(file -> fileRepository.save(file.setPostId(id))));
        }
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
        if (postComment.getPerson().isDeleted()) {
            commentData.setAuthor(setDeletedAuthData(postComment.getPerson()));
        } else if (postComment.getPerson().getId().equals(person.getId()) || !friendshipService.isBlockedBy(postComment.getPerson().getId(), person.getId())) {
            commentData.setAuthor(setAuthData(postComment.getPerson()));
        } else {
            commentData.setAuthor(setBlockerAuthData(postComment.getPerson()));
        }
        commentData.setId(postComment.getId());
        commentData.setTime(postComment.getTime().toInstant(UTC));
        commentData.setDeleted(postComment.isDeleted());
        Set<Like> likes = likeRepository.findLikesByItemAndType(postComment.getId(), "Comment");
        commentData.setLikes(likes.size());
        commentData.setMyLike(likes.stream()
                .anyMatch(commentLike -> commentLike.getPerson().equals(person)));
        if (postComment.getParent() != null)
            commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        commentData.setSubComments(new ArrayList<>());
        List<ImageDto> images = fileRepository.findAll().stream()
                .filter(f -> f.getPostId() != null)
                .filter(file -> file.getPostId().equals(postComment.getId()))
                .map(file -> new ImageDto().setId(String.valueOf(file.getId())).setUrl(file.getUrl()))
                .collect(Collectors.toList());
        commentData.setImages(images);
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

    private void sendNotification(PostComment postComment) {
        Person person;
        SocketNotificationData commentNotificationData = new SocketNotificationData();
        if (postComment.getParent() != null) {
            person = personRepository.findById(getIdFromPostText(postComment.getCommentText())).orElse(postComment.getParent().getPerson());
            commentNotificationData.setEventType(NotificationType.COMMENT_COMMENT)
                    .setParentId(postComment.getParent().getId());
        } else {
            person = postComment.getPost().getPerson();
            commentNotificationData.setParentId(postComment.getId())
                    .setEventType(NotificationType.POST_COMMENT);
        }

        commentNotificationData.setEntityId(postComment.getPost().getId())
                .setEntityAuthor(new AuthorData().setFirstName(postComment.getPerson().getFirstName())
                        .setLastName(postComment.getPerson().getLastName())
                        .setId(postComment.getPerson().getId())
                        .setPhoto(postComment.getPerson().getPhoto()))
                .setCurrentEntityId(postComment.getId())
                .setId(notificationService.createNotification(person, postComment.getId(), commentNotificationData.getEventType()).getId())
                .setSentTime(postComment.getTime().toInstant(UTC))
                .setEventType(commentNotificationData.getEventType());
        notificationService.sendEvent("comment-notification-response", commentNotificationData, person.getId());
    }

    private int getIdFromPostText(String postText) {
        return Integer.parseInt(Arrays.stream(postText.split(","))
                .filter(text -> text.contains("id:")).findFirst().orElse("0000").substring(3));
    }
}
