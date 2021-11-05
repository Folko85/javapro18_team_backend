package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.MessageRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.MessageData;
import com.skillbox.socialnetwork.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/dialogs/{id}/messages")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<MessageData>> getMessages(@PathVariable int id,
                                                                 @RequestParam(name = "query", defaultValue = "") String query,
                                                                 @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                                 @RequestParam(name = "itemPerPage", defaultValue = "1000") int itemPerPage,
                                                                 @RequestParam(name = "fromMessageId", defaultValue = "0") int fromMessageId,
                                                                 Principal principal) {
        return new ResponseEntity<>(messageService.getMessages(id, query, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @PostMapping("/dialogs/{id}/messages")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<MessageData>> postMessages(@PathVariable int id,
                                                    @RequestBody MessageRequest messageRequest,
                                                    Principal principal) {
        return new ResponseEntity<>(messageService.postMessage(id, messageRequest, principal), HttpStatus.OK);
    }

}
