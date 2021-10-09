package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.request.UserRequestModel;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.postdto.PostCreationResponse;
import com.skillbox.socialnetwork.api.response.postdto.PostWallData;
import com.skillbox.socialnetwork.api.response.postdto.PostWallResponse;
import com.skillbox.socialnetwork.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

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
    public ResponseEntity<DataResponse> getUserById(@PathVariable int id) {

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
    public DataResponse updateUser(@RequestBody AuthData person, Principal principal) {
        return userService.updateUser(person, principal);

    }

    @PostMapping("/me")
    public void test() {
        log.info("Teeest");
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

//    @GetMapping("/{id}/wall")
//    @PreAuthorize("hasAuthority('user:write')")
//    public ResponseEntity<PostWallResponse> getUserWall(@PathVariable int id,
//                                                        @RequestParam(name = "offset", defaultValue = "0") int offset,
//                                                        @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage
//    ) {
//
//        List<PostWallData> posts;
//        try {
//            posts = userService.getUserWall(id, offset, itemPerPage);
//        } catch (UsernameNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
//        }
//        PostWallResponse postWallResponse = new PostWallResponse();
//        postWallResponse.setError("string");
//        postWallResponse.setTimestamp(new Date().getTime());
//        postWallResponse.setTotal(posts.size());
//        postWallResponse.setOffset(offset);
//        postWallResponse.setPerPage(itemPerPage);
//        postWallResponse.setData(posts);
//
//        return new ResponseEntity<>(postWallResponse, HttpStatus.OK);
//
//    }

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
