package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.commentdto.CommentData;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@Tag(name = "Контроллер для работы с коментариями")
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{id}/comments")
    @Operation(summary = "Написать коментарий", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<CommentData> postComment(@PathVariable int id,
                                                 @RequestBody CommentRequest commentRequest,
                                                 Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return commentService.postComment(id, commentRequest, principal);
    }

    @PutMapping("/post/{id}/comments/{comment_id}")
    @Operation(summary = "Редактировать коментарий", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<CommentData> putComment(@PathVariable int id,
                                                @PathVariable(name = "comment_id") int commentId,
                                                @RequestBody CommentRequest commentRequest,
                                                Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return commentService.putComment(id, commentId, commentRequest, principal);
    }

    @DeleteMapping("/post/{id}/comments/{comment_id}")
    @Operation(summary = "Удалить коментарий", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<CommentData> deleteComment(@PathVariable int id,
                                                   @PathVariable(name = "comment_id") int commentId,
                                                   Principal principal) throws CommentNotFoundException {
        return commentService.deleteComment(commentId, principal);
    }

    @PutMapping("/post/{id}/comments/{comment_id}/recover")
    @Operation(summary = "Восстановить коментарий", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<CommentData> recoveryComment(@PathVariable int id,
                                                     @PathVariable(name = "comment_id") int commentId,
                                                     Principal principal) throws CommentNotFoundException {
        return commentService.recoveryComment(commentId, principal);
    }

    @GetMapping("/post/{id}/comments")
    @Operation(summary = "Получить коментарии", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<CommentData> getFeeds(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                              @RequestParam(name = "itemPerPage", defaultValue = "5") int itemPerPage,
                                              @PathVariable int id,
                                              Principal principal) throws PostNotFoundException {
        return commentService.getPostComments(offset, itemPerPage, id, principal);
    }
}
