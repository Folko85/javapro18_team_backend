package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
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
@RequestMapping("/api/v1/")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> putLikes(@RequestBody LikeRequest likeRequest,
                                                 Principal principal) throws LikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.putLikes(likeRequest, principal), HttpStatus.OK);
    }

    @DeleteMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> deleteLikes(@RequestParam(name = "item_id") int itemId,
                                                    @RequestParam(name = "type") String type,
                                                    Principal principal) throws LikeNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(likeService.deleteLike(itemId, type, principal), HttpStatus.OK);
    }

    @GetMapping("/likes")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse> getLikes(@RequestParam(name = "item_id") int itemId,
                                                 @RequestParam(name = "type") String type) throws PostNotFoundException {
        return new ResponseEntity<>(likeService.getLikes(itemId, type), HttpStatus.OK);
    }

//    @GetMapping("/liked")
//    @PreAuthorize("hasAuthority('user:write')")
//    public ResponseEntity<DataResponse> getLiked(@RequestParam(name = "user_id") int userId,
//                                                 @RequestParam(name = "item_id") int itemId,
//                                                 @RequestParam(name = "type") String type) throws PostNotFoundException {
//        return new ResponseEntity<>(likeService.getLiked(itemId,userId), HttpStatus.OK);
//    }
}
