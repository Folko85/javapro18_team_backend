package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.DialogRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.service.DialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ListResponse> getDialogs(@RequestParam(name = "name", defaultValue = "") String name,
                                                   @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                   @RequestParam(name = "itemPerPage", defaultValue = "1000") int itemPerPage,
                                                   Principal principal) {
        return new ResponseEntity<>(dialogService.getDialogs(name, offset, itemPerPage, principal), HttpStatus.OK);
    }
    @PostMapping("/dialogs")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> postDialog(@RequestBody DialogRequest dialogRequest, Principal principal) {
        return new ResponseEntity<>(dialogService.postDialog(dialogRequest, principal), HttpStatus.OK);
    }
    @GetMapping("/dialogs/unreaded")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> getUnreaded(Principal principal) {
        return new ResponseEntity<>(dialogService.getUnreaded(principal), HttpStatus.OK);
    }
}
