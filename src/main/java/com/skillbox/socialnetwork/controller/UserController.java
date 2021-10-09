package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;

import com.skillbox.socialnetwork.service.UserService;
import lombok.extern.slf4j.Slf4j;

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
import java.util.Map;


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
}
