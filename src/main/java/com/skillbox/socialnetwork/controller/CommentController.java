package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.CommentDTO.CommentResponse;
import com.skillbox.socialnetwork.api.response.CommentDTO.ParentIdCommentTextRequest;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("/post/{id}/comments")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> getPostComment(@PathVariable int id,
                                            @RequestParam(name = "offset", defaultValue = "0") int offset,
                                            @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage){
        return commentService.getPostComment(id, offset, itemPerPage);
    }


    @PostMapping("/post/{id}/comments")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> postPostComment(@PathVariable int id,
                                                       @RequestBody CommentRequest commentRequest,
                                                       Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return new ResponseEntity<>(commentService.postComment(id, commentRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/api/v1/post/{id}/comments/{comment_id}")
    public ResponseEntity<?> putPostIdCommentsCommentId(@PathVariable int id,
                                                        @PathVariable("comment_id") int commentId,
                                                        @RequestBody ParentIdCommentTextRequest request) {
        return commentService.putPostIdCommentsCommentId(id, commentId, request);
    }
}
