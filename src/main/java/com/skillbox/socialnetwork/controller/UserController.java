package com.skillbox.socialnetwork.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.request.UserUpdateWithInstantRequestModel;

import com.skillbox.socialnetwork.api.request.UserRequestModel;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.postdto.PostCreationResponse;
import com.skillbox.socialnetwork.api.response.postdto.PostWallData;
import com.skillbox.socialnetwork.api.response.postdto.PostWallResponse;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static com.skillbox.socialnetwork.service.UserService.convertLocalDate;

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
    public ResponseEntity<DataResponse> getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setTimestamp(Instant.now());
        AuthData userRest = userService.getUserByEmail(email);
        userRestResponse.setData(userRest);

        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DataResponse> getUserById(@PathVariable String id) {
        Integer userId;
        try {
            userId = Integer.parseInt(id);

        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Path Variable");
        }
        DataResponse userRestResponse = new DataResponse();
        try {
            userRestResponse.setData(userService.getUserById(userId));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<DataResponse> updateUser(HttpEntity<String> httpEntity) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthData updates = new AuthData();
        UserRequestModel userRequestModel = getUserRequestModelFromBody(httpEntity);
        BeanUtils.copyProperties(userRequestModel, updates);
        updates.setEMail(email);
        AuthData updatedUser;
        try {
            updatedUser = userService.updateUser(updates);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setData(updatedUser);
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);

    }

    @PostMapping("/me")
    public void test() {
        System.out.println("Teeest");
    }

    @DeleteMapping("/me")
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

    private UserRequestModel getUserRequestModelFromBody(HttpEntity<String> httpEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        UserRequestModel userRequestModel = new UserRequestModel();
        System.out.println(httpEntity.getBody());
        try {
            ObjectNode node = new ObjectMapper().readValue(httpEntity.getBody(), ObjectNode.class);
            UserUpdateWithInstantRequestModel userUpdateWithInstantRequestModel = objectMapper.readValue(httpEntity.getBody(), UserUpdateWithInstantRequestModel.class);
            if (userUpdateWithInstantRequestModel.getBirthday() != null)
                userRequestModel.setBirthday(convertLocalDate(LocalDate.ofInstant(userUpdateWithInstantRequestModel.getBirthday(), ZoneOffset.UTC)));
            else userRequestModel.setBirthday(0);
            BeanUtils.copyProperties(userUpdateWithInstantRequestModel, userRequestModel);
        } catch (JsonProcessingException e) {
            try {
                userRequestModel = objectMapper.readValue(httpEntity.getBody(), UserRequestModel.class);
            } catch (JsonProcessingException g) {
                throw new IllegalArgumentException();
            }
        }
        return userRequestModel;
    }

}
