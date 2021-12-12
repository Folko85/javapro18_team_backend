package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.postdto.PostData;
import com.skillbox.socialnetwork.exception.PostCreationExecption;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserAndAuthorEqualsException;
import com.skillbox.socialnetwork.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RestController
@Tag(name = "Контроллер для работы с постами")
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    @Operation(summary = "Получить посты в поиске", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<PostData> getPosts(@RequestParam(name = "text", defaultValue = "") String text,
                                           @RequestParam(name = "date_from", defaultValue = "-1") long dateFrom,
                                           @RequestParam(name = "date_to", defaultValue = "-1") long dateTo,
                                           @RequestParam(name = "offset", defaultValue = "0") int offset,
                                           @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                           @RequestParam(name = "author", defaultValue = "") String author,
                                           @RequestParam(name = "tag", defaultValue = "") String tag,
                                           Principal principal) {
        return postService.getPosts(text, dateFrom, dateTo, offset, itemPerPage, author, tag, principal);
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "Получить пост", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<PostData> getPostById(@PathVariable int id, Principal principal) throws PostNotFoundException {
        return postService.getPostById(id, principal);
    }

    @PutMapping("/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Изменить пост", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<PostData> putPostById(@PathVariable int id,
                                              @RequestParam(name = "publish_date", required = false, defaultValue = "0") long publishDate,
                                              @RequestBody PostRequest requestBody,
                                              Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return postService.putPostById(id, publishDate, requestBody, principal);
    }

    @DeleteMapping("/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Удалить пост", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<PostData> deletePostById(@PathVariable int id,
                                                 Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return postService.deletePostById(id, principal);
    }


    @PutMapping("/post/{id}/recover")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Восстановить пост", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<PostData> putPostRecover(@PathVariable int id,
                                                 Principal principal) throws PostNotFoundException, UserAndAuthorEqualsException {
        return postService.putPostIdRecover(id, principal);
    }


    @GetMapping("/feeds")
    @Operation(summary = "Получить посты новостях", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<PostData> getFeeds(@RequestParam(name = "text", defaultValue = "") String text,
                                           @RequestParam(name = "offset", defaultValue = "0") int offset,
                                           @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                           Principal principal) {
        return postService.getFeeds(text, offset, itemPerPage, principal);
    }

    @GetMapping("/users/{id}/wall")
    @Operation(summary = "Получить посты на стене", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<PostData> getUserWall(@PathVariable int id,
                                              @RequestParam(name = "offset", defaultValue = "0") int offset,
                                              @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage,
                                              Principal principal) {
        return postService.getPersonWall(id, offset, itemPerPage, principal);
    }

    @PostMapping("/users/{id}/wall")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Создать пост на стене", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<PostData> getUserWall(@PathVariable int id,
                                              @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
                                              @RequestBody PostRequest postRequest, Principal principal) throws PostCreationExecption {
        return postService.createPost(id, publishDate, postRequest, principal);
    }
}
