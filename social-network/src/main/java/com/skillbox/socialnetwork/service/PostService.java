package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.postdto.PostData;
import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.exception.PostCreationExecption;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserAndAuthorEqualsException;
import com.skillbox.socialnetwork.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.singletonList;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PersonRepository personRepository;
    private final CommentService commentService;
    private final LikeRepository likeRepository;
    private final FriendshipService friendshipService;
    private final TagRepository tagRepository;
    private final FileRepository fileRepository;

    private static final Pattern pattern = Pattern.compile("<img\\s+[^>]*src=\"([^\"]*)\"[^>]*>");

    public ListResponse<PostData> getPosts(String text, long dateFrom, long dateTo, int offset, int itemPerPage, String author, String tag, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Instant datetimeTo = (dateTo == -1) ? Instant.now() : Instant.ofEpochMilli(dateTo);
        Instant datetimeFrom = (dateFrom == -1) ? ZonedDateTime.now().minusYears(1).toInstant() : Instant.ofEpochMilli(dateFrom);
        List<Integer> blockers = personRepository.findBlockersIds(person.getId());
        blockers = !blockers.isEmpty() ? blockers : singletonList(-1);
        Page<Post> pageablePostList;
        if (tag.equals("")) {
            pageablePostList = postRepository.findPostsByTextContainingByDateExcludingBlockersWithoutTags(text, author,
                    datetimeFrom, datetimeTo, pageable, blockers);
        } else {
            List<Integer> tags = Arrays.stream(tag.split("_"))
                    .map(t -> tagRepository.findByTag(t).orElse(null))
                    .filter(Objects::nonNull).map(Tag::getId).collect(Collectors.toList());
            pageablePostList = postRepository.findPostsByTextContainingByDateExcludingBlockers(text, author, datetimeFrom,
                    datetimeTo, pageable, blockers, tags, tags.size());
        }

        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    private Post findPost(int itemId) throws PostNotFoundException {
        return postRepository.findById(itemId)
                .orElseThrow(PostNotFoundException::new);
    }


    public DataResponse<PostData> putPostById(int id, long publishDate, PostRequest requestBody, Principal
            principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = findPost(id);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setTitle(requestBody.getTitle());
        post.setPostText(requestBody.getPostText());
        List<String> tags = requestBody.getTags();
        if (tags != null) {
            post.setTags(tags.stream().map(s -> tagRepository.findByTag(s).orElse(null))
                    .filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        post.setDatetime(Instant.ofEpochMilli(publishDate == 0 ? System.currentTimeMillis() : publishDate));
        post = postRepository.saveAndFlush(post);
        Matcher images = pattern.matcher(requestBody.getPostText());
        while (images.find()) {
            PostFile file = fileRepository.findByUrl(images.group(1));
            fileRepository.save(file.setPostId(post.getId()));
        }
        return getPostDataResponse(post, person);
    }

    public DataResponse<PostData> deletePostById(int id, Principal principal) throws
            PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setDeleted(true);
        post.setDeletedTimestamp(LocalDateTime.now());
        postRepository.saveAndFlush(post);
        return getPostDataResponse(post, person);
    }

    public DataResponse<PostData> putPostIdRecover(int id, Principal principal) throws
            PostNotFoundException, UserAndAuthorEqualsException {
        Person person = findPerson(principal.getName());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!person.getId().equals(post.getPerson().getId())) throw new UserAndAuthorEqualsException();
        post.setDeleted(false);
        postRepository.saveAndFlush(post);
        return getPostDataResponse(post, person);
    }

    public ListResponse<PostData> getFeeds(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        List<Integer> friendsAndFriendsOfFriendsAndSubscribesFilteredIds = friendshipService.getFriendsAndFriendsOfFriendsAndSubscribesFiltered(person.getId());
        friendsAndFriendsOfFriendsAndSubscribesFilteredIds.add(person.getId());
        Page<Post> pageablePostList = postRepository.findPostsByTextContainingExcludingBlockers(text, pageable, friendsAndFriendsOfFriendsAndSubscribesFilteredIds);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public ListResponse<PostData> getPersonWall(int id, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList;
        if (id == person.getId()) {
            pageablePostList = postRepository.findPostsByPersonId(id, pageable);
        } else if (!friendshipService.isBlockedBy(id, person.getId()) && !person.isDeleted()) {
            pageablePostList = postRepository.findPostsByPersonIdAndCurrentDate(id, pageable);
        } else {
            pageablePostList = Page.empty();
        }

        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }

    public DataResponse<PostData> getPostById(int id, Principal principal) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Person person = personRepository.findByEMail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        DataResponse<PostData> dataResponse = new DataResponse<>();
        dataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dataResponse.setData(getPostData(post, person));
        return dataResponse;
    }

    private ListResponse<PostData> getPostResponse(int offset, int itemPerPage, Page<Post> pageablePostList, Person person) {
        ListResponse<PostData> postResponse = new ListResponse<>();
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
        postData.setComments(commentService.getPage4PostComments(0, 5, post, person));
        postData.setId(post.getId());
        Set<Like> likes = likeRepository.findLikesByItemAndType(post.getId(), "Post");
        postData.setLikes(likes.size());
        postData.setTime(post.getDatetime());
        postData.setTitle(post.getTitle());
        postData.setBlocked(post.isBlocked());
        if (post.getTags() != null) {
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
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private DataResponse<PostData> getPostDataResponse(Post post, Person person) {
        DataResponse<PostData> postDataResponse = new DataResponse<>();
        postDataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postDataResponse.setData(getPostData(post, person));

        return postDataResponse;
    }

    public DataResponse<PostData> createPost(int id, long publishDate, PostRequest postRequest, Principal principal) throws PostCreationExecption {
        Person person = findPerson(principal.getName());
        if (person.getId() != id) throw new PostCreationExecption();
        Post post = new Post();
        post.setPostText(postRequest.getPostText());
        post.setTitle(postRequest.getTitle());
        List<String> tags = postRequest.getTags();
        if (tags != null) {
            post.setTags(tags.stream()
                    .map(x -> tagRepository.findByTag(x).orElse(null))
                    .filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        if (publishDate == 0) {
            post.setDatetime(Instant.now());
        } else {
            post.setDatetime(Instant.ofEpochMilli(publishDate));
        }
        post.setPerson(person);
        Post createdPost = postRepository.save(post);
        Matcher images = pattern.matcher(postRequest.getPostText());
        while (images.find()) {
            PostFile file = fileRepository.findByUrl(images.group(1));
            fileRepository.save(file.setPostId(createdPost.getId()));
        }
        DataResponse<PostData> dataResponse = new DataResponse<>();
        dataResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        dataResponse.setData(getPostData(createdPost, person));
        return dataResponse;
    }

    public void deletePostAfterSoft(Post post) {
        post.setTitle("Deleted");
        post.setPostText("Deleted");
        post.setDeleted(false);
        post.setDeletedTimestamp(null);
        postRepository.save(post);
    }
}
