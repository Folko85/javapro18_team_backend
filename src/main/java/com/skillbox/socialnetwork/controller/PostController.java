package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.request.TitlePostTextRequest;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.exception.PostCreationExecption;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserAndAuthorEqualsException;
import com.skillbox.socialnetwork.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер для работы с постами")
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    @Operation(summary = "Получить посты в поиске")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> getPosts(@RequestParam(name = "text", defaultValue = "") String text,
                                                 @RequestParam(name = "date_from", defaultValue = "0") long dateFrom,
                                                 @RequestParam(name = "date_to", defaultValue = "1701214256861") long dateTo,
                                                 @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                 @RequestParam(name = "author", defaultValue = "") String author,
                                                 Principal principal) {
        return new ResponseEntity<>(postService.getPosts(text, dateFrom, dateTo, offset, itemPerPage, author, principal), HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "Получить пост")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> getPostById(@PathVariable int id, Principal principal) throws PostNotFoundException {
        return new ResponseEntity<>(postService.getPostById(id, principal), HttpStatus.OK);
    }

    @PutMapping("/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> putPostById(@PathVariable int id,
                                         @RequestParam(name = "publish_date", required = false, defaultValue = "0") long publishDate,
                                         @RequestBody TitlePostTextRequest requestBody,
                                         Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return new ResponseEntity<>(postService.putPostById(id, publishDate, requestBody, principal), HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> deletePostById(@PathVariable int id,
                                            Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return new ResponseEntity<>(postService.deletePostById(id, principal), HttpStatus.OK);
    }


    @PutMapping("/post/{id}/recover")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> putPostRecover(@PathVariable int id,
                                            Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return new ResponseEntity<>(postService.putPostIdRecover(id, principal), HttpStatus.OK);
    }


    @GetMapping("/feeds")
    @Operation(summary = "Получить посты новостях")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> getFeeds(@RequestParam(name = "text", defaultValue = "") String text,
                                                 @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                 Principal principal) {
        return new ResponseEntity<>(postService.getFeeds(text, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/wall")
    @Operation(summary = "Получить посты на стене")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> getUserWall(@PathVariable int id,
                                                    @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                    @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage,
                                                    Principal principal) {
        return new ResponseEntity<>(postService.getPersonWall(id, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @PostMapping("/users/{id}/wall")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Создать пост на стене")
    public ResponseEntity<DataResponse> getUserWall(@PathVariable int id,
                                                    @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
                                                    @RequestBody PostRequest postRequest, Principal principal) throws PostCreationExecption {
        return new ResponseEntity<>(postService.createPost(id, publishDate, postRequest, principal), HttpStatus.OK);
    }
}
