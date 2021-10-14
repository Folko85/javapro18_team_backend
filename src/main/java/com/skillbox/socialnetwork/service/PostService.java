package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.request.TitlePostTextRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.postdto.IdResponse;
import com.skillbox.socialnetwork.api.response.postdto.PostData;
import com.skillbox.socialnetwork.api.response.postdto.PostDataResponse;
import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.exception.PostCreationExecption;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserAndAuthorEqualsException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
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
                Instant.ofEpochMilli(dateFrom)
                , Instant.ofEpochMilli(dateTo),
                pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    private Post findPost(int itemId) throws PostNotFoundException {
        return postRepository.findById(itemId)
                .orElseThrow(PostNotFoundException::new);
    }


    public DataResponse putPostById(int id, long publishDate, TitlePostTextRequest requestBody, Principal
            principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = findPost(id);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setTitle(requestBody.getTitle());
        post.setPostText(requestBody.getPostText());
        post.setDatetime(Instant.ofEpochMilli(publishDate == 0 ? System.currentTimeMillis() : publishDate));
        postRepository.saveAndFlush(post);

        return getPostDataResponse(post, person);
    }

    public DataResponse deletePostById(int id, Principal principal) throws
            PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setDeleted(true);
        postRepository.saveAndFlush(post);
        return getPostDataResponse(post, person);
    }

    public PostDataResponse putPostIdRecover(int id, Principal principal) throws
            PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setDeleted(false);
        postRepository.saveAndFlush(post);

        return getPostDataResponseForDeleted();
    }

    private PostDataResponse getPostDataResponseForDeleted() {
        PostDataResponse postDataResponse = new PostDataResponse();
        postDataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC).toEpochMilli());
        IdResponse idResponse = new IdResponse();
        postDataResponse.setData(idResponse);
        return postDataResponse;
    }

    public ListResponse getFeeds(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContaining(text, pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public ListResponse getPersonWall(int id, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<Post> pageablePostList = id == person.getId() ?
                postRepository.findPostsByPersonId(id, pageable) : postRepository.findPostsByPersonIdAndCurrentDate(id, pageable);
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
        postData.setTime(post.getDatetime());
        postData.setTitle(post.getTitle());
        postData.setBlocked(post.isBlocked());
        if (post.getTags() != null){
            postData.setTags(post.getTags().stream().map(Tag::getTag).collect(Collectors.toList()));
        }
        postData.setMyLike(likes.stream()
                .anyMatch(postLike -> postLike.getPerson().equals(person)));
        if (Instant.now().isBefore(post.getDatetime())) {
            postData.setType("QUEUED");
        } else postData.setType("POSTED");
        return postData;
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private DataResponse getPostDataResponse(Post post, Person person) {
        DataResponse postDataResponse = new DataResponse();
        postDataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postDataResponse.setData(getPostData(post, person));

        return postDataResponse;
    }

    public DataResponse createPost(int id, long publishDate, PostRequest postRequest, Principal principal) throws PostCreationExecption {
        Person person = findPerson(principal.getName());
        if (person.getId() != id) throw new PostCreationExecption();
        Post post = new Post();
        post.setPostText(postRequest.getPostText());
        post.setTitle(postRequest.getTitle());
        if (publishDate == 0) {
            post.setDatetime(Instant.now());
        } else {
            post.setDatetime(Instant.ofEpochMilli(publishDate));
        }
        post.setPerson(person);
        Post createdPost = postRepository.save(post);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dataResponse.setData(getPostData(createdPost, person));
        return dataResponse;
    }
}
