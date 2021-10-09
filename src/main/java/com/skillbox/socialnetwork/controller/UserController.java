package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.authdto.UserDeleteResponse;
import com.skillbox.socialnetwork.api.response.authdto.UserRest;
import com.skillbox.socialnetwork.api.response.authdto.UserRestResponse;
import com.skillbox.socialnetwork.api.request.UserRequestModel;
import com.skillbox.socialnetwork.service.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UserController {
    private  UserServiceImpl userService;
    public UserController(UserServiceImpl userService){
        this.userService= userService;

    }

    @GetMapping("/me")
    public ResponseEntity<UserRestResponse> getMe() throws Exception {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserRestResponse userRestResponse = new UserRestResponse();
            userRestResponse.setTimestamp(new Date().getTime() / 1000);
            userRestResponse.setError("null");
            userRestResponse.setData(userService.getUserByEmail(email));
        System.out.println("####Principal##########:");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new ResponseEntity<UserRestResponse>(userRestResponse, HttpStatus.OK);
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
    public  ResponseEntity<UserRestResponse> updateUser(@RequestBody UserRequestModel userRequestModel){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRest updates= new UserRest();
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

}
