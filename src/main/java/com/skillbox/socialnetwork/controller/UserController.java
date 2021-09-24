package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UserController {
    private  UserServiceImpl userService;
    public UserController(UserServiceImpl userService){
        this.userService= userService;

    }

    @GetMapping("/me")
    public ResponseEntity<UserRest> login() throws Exception {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }


}
