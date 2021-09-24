package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/me")
    public UserRest getInfo() throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(email);
    }

    @PutMapping("/me")
    public UserRest changeInfo(@RequestBody UserRest userUpdated) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.updateUserByEmail(email, userUpdated);
    }


}
