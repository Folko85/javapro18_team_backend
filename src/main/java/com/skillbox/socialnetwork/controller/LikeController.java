package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер для работы с лайками")
@RequestMapping("/api/v1/")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/likes")
    @Operation(summary = "Поставить лайк")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> putLikes(@RequestBody LikeRequest likeRequest,
                                                 Principal principal) throws LikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.putLikes(likeRequest, principal), HttpStatus.OK);
    }

    @DeleteMapping("/likes")
    @Operation(summary = "Удалить лайк")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> deleteLikes(@RequestParam(name = "item_id") int itemId,
                                                    @RequestParam(name = "type") String type,
                                                    Principal principal) throws LikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.deleteLike(itemId, type, principal), HttpStatus.OK);
    }

    @GetMapping("/likes")
    @Operation(summary = "Получить лайки")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> getLikes(@RequestParam(name = "item_id") int itemId,
                                                 @RequestParam(name = "type") String type) throws PostNotFoundException {
        return new ResponseEntity<>(likeService.getLikes(itemId, type), HttpStatus.OK);
    }
}
