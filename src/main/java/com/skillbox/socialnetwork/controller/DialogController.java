package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class DialogController {
    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                                   @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                   @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                   Principal principal) {
        return new ResponseEntity<>(friendshipService.getFriends(name, offset, itemPerPage, principal), HttpStatus.OK);
    }
}
