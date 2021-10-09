package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.PostRequest;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostCreationResponse;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallResponse;
import com.skillbox.socialnetwork.exception.UpdatedAuthDataIdIsNotEqualPrincipalId;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;

    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> getMe(Principal principal) {
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setTimestamp(Instant.now());
        AuthData userRest = userService.getUserByEmail(principal);
        userRestResponse.setData(userRest);

        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> getUserById(@PathVariable int id) {
        DataResponse userRestResponse = new DataResponse();
        try {
            userRestResponse.setData(userService.getUserById(id));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> updateUser(@RequestBody AuthData updates, Principal principal ) throws UpdatedAuthDataIdIsNotEqualPrincipalId {
        AuthData updatedUser  = userService.updateUser(updates, principal);
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setData(updatedUser);
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            userService.deleteUser(email);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        AccountResponse userDeleteResponse = new AccountResponse();
        userDeleteResponse.setTimestamp(Instant.now());
        userDeleteResponse.setError("string");
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        userDeleteResponse.setData(dateMap);
        SecurityContextHolder.clearContext();
        return new ResponseEntity(userDeleteResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/wall")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostWallResponse> getUserWall(@PathVariable int id,
                                                        @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage
    ) {

        List<PostWallData> posts;
        try {
            posts = userService.getUserWall(id, offset, itemPerPage);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        PostWallResponse postWallResponse = new PostWallResponse();
        postWallResponse.setError("string");
        postWallResponse.setTimestamp(new Date().getTime());
        postWallResponse.setTotal(posts.size());
        postWallResponse.setOffset(offset);
        postWallResponse.setPerPage(itemPerPage);
        postWallResponse.setData(posts);

        return new ResponseEntity<>(postWallResponse, HttpStatus.OK);

    }

    @PostMapping("/{id}/wall")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostCreationResponse> getUserWall(@PathVariable int id,
                                                            @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
                                                            @RequestBody PostRequest postRequest, Principal principal
    ) {

        PostCreationResponse postCreationResponse = new PostCreationResponse();
        postCreationResponse.setTimestamp(new Date().getTime());
        PostWallData postWallData = userService.createPost(id, publishDate, postRequest, principal);
        postCreationResponse.setData(postWallData);
        return new ResponseEntity<>(postCreationResponse, HttpStatus.OK);
    }
}
