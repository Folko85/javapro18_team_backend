package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{id}/comments")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> postComment(@PathVariable int id,
                                                    @RequestBody CommentRequest commentRequest,
                                                    Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return new ResponseEntity<>(commentService.postComment(id, commentRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/post/{id}/comments/{comment_id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> putComment(@PathVariable int id,
                                                   @PathVariable(name = "comment_id") int commentId,
                                                   @RequestBody CommentRequest commentRequest,
                                                   Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return new ResponseEntity<>(commentService.putComment(id, commentId, commentRequest, principal), HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}/comments/{comment_id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> deleteComment(@PathVariable int id,
                                                      @PathVariable(name = "comment_id") int commentId,
                                                      Principal principal) throws CommentNotFoundException {
        return new ResponseEntity<>(commentService.deleteComment(id, commentId, principal), HttpStatus.OK);
    }

    @PutMapping("/post/{id}/comments/{comment_id}/recover")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> recoveryComment(@PathVariable int id,
                                                        @PathVariable(name = "comment_id") int commentId,
                                                        Principal principal) throws CommentNotFoundException {
        return new ResponseEntity<>(commentService.recoveryComment(id, commentId, principal), HttpStatus.OK);
    }
}
