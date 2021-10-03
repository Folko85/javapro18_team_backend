package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.LikeDTO.LikeResponse;
import com.skillbox.socialnetwork.exception.PostLikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeResponse> putLikes(@RequestBody LikeRequest likeRequest,
                                                 Principal principal) throws PostLikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.putLikes(likeRequest, principal), HttpStatus.OK);
    }

    @DeleteMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeResponse> deleteLikes(@RequestParam(name = "item_id") int itemId,
                                                    @RequestParam(name = "type") String type,
                                                    Principal principal) throws PostLikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.deleteLike(itemId, principal), HttpStatus.OK);
    }

    @GetMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeResponse> getLikes(@RequestParam(name = "item_id") int itemId,
                                                 @RequestParam(name = "type") String type) throws PostNotFoundException {
        return new ResponseEntity<>(likeService.getLikes(itemId), HttpStatus.OK);
    }

    @GetMapping("/liked")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeResponse> getLiked(@RequestParam(name = "user_id") int userId,
                                                 @RequestParam(name = "item_id") int itemId,
                                                 @RequestParam(name = "type") String type) throws PostNotFoundException {
        return new ResponseEntity<>(likeService.getLiked(itemId,userId), HttpStatus.OK);
    }
}