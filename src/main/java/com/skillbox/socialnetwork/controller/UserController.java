package com.skillbox.socialnetwork.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.socialnetwork.api.request.UserUpdateWithInstantRequestModel;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserDeleteResponse;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRestResponse;
import com.skillbox.socialnetwork.api.request.UserRequestModel;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallResponse;
import com.skillbox.socialnetwork.service.UserServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static com.skillbox.socialnetwork.service.UserServiceImpl.convertLocalDate;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UserController {
    private  UserServiceImpl userService;
    public UserController(UserServiceImpl userService){
        this.userService= userService;

    }

    /**
     * Возвращает UserRest, что бы фронт смог обработать пользователя.
     * Для соотвествия требованиям API необходимо доавить UserRest в UserRestResponse и поменять тип метода.
     * @return UserRest userRest
     * @throws Exception
     */
    @GetMapping("/me")
    public ResponseEntity<UserRest> getMe() throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRestResponse userRestResponse = new UserRestResponse();
        userRestResponse.setTimestamp(new Date().getTime() / 1000);
        userRestResponse.setError("null");
        UserRest userRest = userService.getUserByEmail(email);
        userRestResponse.setData(userRest);

        return new ResponseEntity<>(userRest, HttpStatus.OK);
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
    /**
     * Возвращает UserRest, что бы фронт смог обработать пользователя.
     * Для соотвествия требованиям API необходимо доавить UserRest в UserRestResponse и поменять тип метода.
     * @return UserRest updatedUser
     * @throws Exception
     */
    @PutMapping("/me")
    /**Замени аргумент updateUser() на (@RequestBody UserRequestModel userRequestModel),
     * удали вызов и сам метод getUserRequestModelFromBody(), и не будет костыля.
     * plusMonths(1) необходимо удалить, если не будет фронт убирать лишний месяц.
     * Удали UserUpdateWithInstantRequestModel.
     */
    public  ResponseEntity<UserRest> updateUser(HttpEntity<String> httpEntity){
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
        return  new ResponseEntity<>(updatedUser, HttpStatus.OK);

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
        postWallResponse.setTimestamp(new Date().getTime() / 1000);
        postWallResponse.setTotal(posts.size());
        postWallResponse.setOffset(offset);
        postWallResponse.setPerPage(itemPerPage);
        postWallResponse.setData(posts);

        return new ResponseEntity<>(postWallResponse, HttpStatus.OK);

    }
    @PostMapping("/{id}/wall")
    public ResponseEntity<PostWallResponse> getUserWall(@PathVariable int id){

        PostWallResponse postWallResponse = new PostWallResponse();

        return  new ResponseEntity<>(postWallResponse, HttpStatus.OK);
    }


    private  UserRequestModel getUserRequestModelFromBody(HttpEntity<String> httpEntity){
        ObjectMapper objectMapper = new ObjectMapper(); objectMapper.registerModule(new JavaTimeModule());
        UserRequestModel userRequestModel =new UserRequestModel();
        System.out.println(httpEntity.getBody());
        try {
            UserUpdateWithInstantRequestModel userUpdateWithInstantRequestModel= objectMapper.readValue(httpEntity.getBody(), UserUpdateWithInstantRequestModel.class);
            userRequestModel.setBirthday(convertLocalDate(LocalDate.ofInstant(userUpdateWithInstantRequestModel.getBirthday(), ZoneOffset.UTC).plusMonths(1)));
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
