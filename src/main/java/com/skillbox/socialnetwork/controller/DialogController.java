package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.service.DialogService;
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
    private final DialogService dialogService;

    public DialogController(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    @GetMapping("/dialogs")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                                   @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                   @RequestParam(name = "itemPerPage", defaultValue = "10") int itemPerPage,
                                                   Principal principal) {
        return new ResponseEntity<>(dialogService.getDialogs(name, offset, itemPerPage, principal), HttpStatus.OK);
    }
}
