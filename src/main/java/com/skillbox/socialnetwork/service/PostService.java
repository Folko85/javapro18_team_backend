package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.LikeRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static com.skillbox.socialnetwork.service.CommentService.getCommentWallData4Response;
import static com.skillbox.socialnetwork.service.UserService.convertLocalDateTime;
import static com.skillbox.socialnetwork.service.UserService.convertToLocalDateTime;
import static java.time.ZoneOffset.UTC;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final CommentService commentService;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, AccountRepository accountRepository, CommentService commentService, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.commentService = commentService;
        this.likeRepository = likeRepository;

    }

    public ListResponse getPosts(String text, long dateFrom, long dateTo, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContainingByDate(text,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(dateFrom), UTC)
                , LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTo), UTC),
                pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public ListResponse getFeeds(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContaining(text, pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public DataResponse getPostById(int id, Principal principal) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Person person = accountRepository.findByEMail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        DataResponse dataResponse = new DataResponse();
        dataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dataResponse.setData(getPostData(post, person));
        return dataResponse;
    }

    private ListResponse getPostResponse(int offset, int itemPerPage, Page<Post> pageablePostList, Person person) {
        ListResponse postResponse = new ListResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePostList.getTotalElements());
        postResponse.setData(getPost4Response(pageablePostList.toList(), person));
        return postResponse;
    }

    private List<Dto> getPost4Response(List<Post> posts, Person person) {
        List<Dto> postDataList = new ArrayList<>();
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
        postData.setComments(commentService.getPage4PostComments(0, 5, post, person));
        postData.setId(post.getId());
        Set<Like> likes = likeRepository.findLikesByItemAndType(post.getId(), "Post");
        postData.setLikes(likes.size());
        postData.setTime(post.getDatetime().toInstant(UTC));
        postData.setTitle(post.getTitle());
        postData.setBlocked(post.isBlocked());
        postData.setTags(List.of("tag","tagtagtagtagtagtag","tag","tag","tag","tag","tag","tag"));
        postData.setMyLike(likes.stream()
                .anyMatch(postLike -> postLike.getPerson().equals(person)));
        return postData;
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    public PostWallData createPost(long publishDate, PostRequest postRequest, AuthData userRest) {

        Post post = new Post();
        post.setPostText(postRequest.getPostText());
        post.setTitle(postRequest.getTitle());
        LocalDateTime publisDateTime = convertToLocalDateTime(publishDate);
        if (publisDateTime == null) {
            post.setDatetime(LocalDateTime.now(ZoneOffset.UTC));
        } else {
            post.setDatetime(publisDateTime);
        }
        Person person = new Person();
        person.setId(userRest.getId());
        post.setBlocked(false);
        post.setPerson(person);
        Post saved = postRepository.save(post);
        PostWallData postWallData = getPostWallData(saved, userRest);
        return postWallData;
    }

    public List<PostWallData> getPastWallData(Integer offset, Integer itemPerPage, AuthData userRest) {
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> page = postRepository.findUserPost(userRest.getId(), pageable);
        List<Post> posts = page.getContent();
        List<PostWallData> postWallDataList = new ArrayList<>();
        posts.forEach(post -> {
            PostWallData postWallData = getPostWallData(post, userRest);
            postWallDataList.add(postWallData);
        });
        return postWallDataList;
    }

    public PostWallData getPostWallData(Post post, AuthData userRest) {
        PostWallData postWallData = new PostWallData();
        postWallData.setPostText(post.getPostText());
        postWallData.setAuthor(userRest);
        postWallData.setComments(getCommentWallData4Response(post.getComments()));
        postWallData.setId(post.getId());
        postWallData.setTime(convertLocalDateTime(post.getDatetime()));
        postWallData.setTitle(post.getTitle());
        postWallData.setBlocked(post.isBlocked());
        if (LocalDateTime.now().isBefore(post.getDatetime())) {
            postWallData.setType("QUEUED");
        } else {
            postWallData.setType("POSTED");
        }
        if (post.isBlocked()) {
            postWallData.setType("BLOCKED");
        }
        return postWallData;
    }

}
