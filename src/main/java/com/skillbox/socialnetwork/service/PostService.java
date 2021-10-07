package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.TitlePostTextRequest;
import com.skillbox.socialnetwork.api.response.PostDTO.IdResponse;
import com.skillbox.socialnetwork.api.response.PostDTO.PostData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostDataResponse;
import com.skillbox.socialnetwork.api.response.PostDTO.PostResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserAndAuthorEqualsException;
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
import static java.time.ZoneOffset.UTC;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public PostService(PostRepository postRepository,
                       AccountRepository accountRepository) {
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
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public PostDataResponse getPostById(int id, Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findPostById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        return getPostDataResponse(post, person);
    }


    public PostDataResponse putPostById(int id, long publishDate, TitlePostTextRequest requestBody, Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setTitle(requestBody.getTitle());
        post.setPostText(requestBody.getPostText());
        post.setDatetime(Instant.ofEpochMilli(publishDate == 0 ? System.currentTimeMillis() : publishDate));
        postRepository.saveAndFlush(post);

        return getPostDataResponse(post, person);
    }
    public PostDataResponse deletePostById(int id, Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setDeleted(true);
        postRepository.saveAndFlush(post);
        return getPostDataResponse(post, person);
    }

    public PostDataResponse putPostIdRecover(int id,  Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
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

    public PostResponse getFeeds(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContaining(text, pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    private PostResponse getPostResponse(int offset, int itemPerPage, Page<Post> pageablePostList, Person person) {
        PostResponse postResponse = new PostResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC).toEpochMilli());
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
        postData.setTime(post.getDatetime());
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

    private PostDataResponse getPostDataResponse(Post post, Person person) {
        PostDataResponse postDataResponse = new PostDataResponse();
        PostData postData = getPostData(post, person);
        postDataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC).toEpochMilli());
        postDataResponse.setData(postData);

        return postDataResponse;
    }
}
