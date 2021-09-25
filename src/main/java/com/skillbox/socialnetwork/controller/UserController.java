package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthResponse;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRestResponse;
import com.skillbox.socialnetwork.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

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
        return new ResponseEntity<UserRestResponse>(userRestResponse, HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserRestResponse> getUserById(@PathVariable String id) throws Exception {
        Integer userId;
        try{
            userId = Integer.valueOf(id);

        } catch (NumberFormatException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Path Variable");
        }
        UserRestResponse userRestResponse = new UserRestResponse();
        try {
            userRestResponse.setData(userService.getUserById(userId));
        }
        catch (UsernameNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        userRestResponse.setTimestamp(new Date().getTime() / 1000);
        userRestResponse.setError("null");
        return new ResponseEntity<>(userRestResponse, HttpStatus.OK);
    }
}
