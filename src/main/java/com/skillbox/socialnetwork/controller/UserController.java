package com.skillbox.socialnetwork.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.request.UserUpdateWithInstantRequestModel;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserDeleteResponse;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRestResponse;
import com.skillbox.socialnetwork.api.request.UserRequestModel;
import com.skillbox.socialnetwork.api.response.PostDTO.PostCreationResponse;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallResponse;
import com.skillbox.socialnetwork.service.PostService;
import com.skillbox.socialnetwork.service.UserServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static com.skillbox.socialnetwork.service.UserServiceImpl.convertLocalDate;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UserController {
    private  UserServiceImpl userService;
    private PostService postService;
    public UserController(UserServiceImpl userService, PostService postService){
        this.userService= userService;
        this.postService=postService;

    }

    @GetMapping("/me")
    public ResponseEntity<UserRestResponse> getMe() throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRestResponse userRestResponse = new UserRestResponse();
        userRestResponse.setTimestamp(new Date().getTime() / 1000);
        userRestResponse.setError("null");
        UserRest userRest = userService.getUserByEmail(email);
        userRestResponse.setData(userRest);

        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserRestResponse> getUserById(@PathVariable String id) throws Exception {
        Integer userId;
        try{
            userId = Integer.valueOf(id);

        } catch (NumberFormatException e){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Path Variable");
        }
        UserRestResponse userRestResponse = new UserRestResponse();
        try {
            userRestResponse.setData(userService.getUserById(userId));
        }
        catch (UsernameNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        userRestResponse.setTimestamp(new Date().getTime() / 1000);
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }

    @PutMapping("/me")
    public  ResponseEntity<UserRestResponse> updateUser(HttpEntity<String> httpEntity){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRest updates= new UserRest();
        UserRequestModel userRequestModel = getUserRequestModelFromBody(httpEntity);
        BeanUtils.copyProperties(userRequestModel, updates);
        updates.setEMail(email);
        UserRest updatedUser;
        try {
            updatedUser = userService.updateUser(updates);
        }
        catch (UsernameNotFoundException e){
            throw  new  ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        UserRestResponse userRestResponse = new UserRestResponse();
        userRestResponse.setData(updatedUser);
        userRestResponse.setTimestamp(new Date().getTime() / 1000);
        userRestResponse.setError("null");
        return  new ResponseEntity<>(userRestResponse, HttpStatus.OK);

    }

    @PostMapping("/me")
    public  void test(){
        System.out.println("Teeest");
    }

    @DeleteMapping("/me")
    public  ResponseEntity<UserDeleteResponse> deleteUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            userService.deleteUser(email);
        }
        catch (UsernameNotFoundException e){
            throw  new  ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse();
        userDeleteResponse.setTimestamp(new Date().getTime() / 1000);
        userDeleteResponse.setError("string");
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        userDeleteResponse.setData(dateMap);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(userDeleteResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<PostWallResponse> getUserWall(@PathVariable int id,
                             @RequestParam(name = "offset", defaultValue = "0") int offset,
                             @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage
                             ){
        Integer userId;
        try{
            userId = Integer.valueOf(id);

        } catch (NumberFormatException e){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Path Variable");
        }
        List<PostWallData> posts =new ArrayList<>();
        try {
           posts= userService.getUserWall(id, offset, itemPerPage);
        }
        catch (UsernameNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
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
                            ){

        PostCreationResponse postCreationResponse = new PostCreationResponse();
        postCreationResponse.setTimestamp(new Date().getTime());
        PostWallData postWallData = userService.createPost(id, publishDate, postRequest, principal);
        postCreationResponse.setData(postWallData);
        return  new ResponseEntity<>(postCreationResponse, HttpStatus.OK);
    }

    private  UserRequestModel getUserRequestModelFromBody(HttpEntity<String> httpEntity) {
        ObjectMapper objectMapper = new ObjectMapper(); objectMapper.registerModule(new JavaTimeModule());
        UserRequestModel userRequestModel =new UserRequestModel();
        System.out.println(httpEntity.getBody());
        try {
            ObjectNode node = new ObjectMapper().readValue(httpEntity.getBody(), ObjectNode.class);
            UserUpdateWithInstantRequestModel userUpdateWithInstantRequestModel= objectMapper.readValue(httpEntity.getBody(), UserUpdateWithInstantRequestModel.class);
            if(userUpdateWithInstantRequestModel.getBirthday()!=null)
                userRequestModel.setBirthday(convertLocalDate(LocalDate.ofInstant(userUpdateWithInstantRequestModel.getBirthday(), ZoneOffset.UTC)));
            else userRequestModel.setBirthday(0);
            BeanUtils.copyProperties(userUpdateWithInstantRequestModel, userRequestModel);
        }
        catch (JsonProcessingException e){
            try{
                userRequestModel= objectMapper.readValue(httpEntity.getBody(), UserRequestModel.class);
            }
            catch (JsonProcessingException g){throw  new IllegalArgumentException();}
        }
        return userRequestModel;
    }

}
