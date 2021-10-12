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

        return new ResponseEntity<>(userService.getUserMe(principal), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> getUserById(@PathVariable int id) {

        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse updateUser(@RequestBody AuthData person, Principal principal) {
        return userService.updateUser(person, principal);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> deleteUser(Principal principal) {
        return new ResponseEntity(userService.deleteUser(principal), HttpStatus.OK);
    }
}
