package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.PostDTO.PostResponse;
import com.skillbox.socialnetwork.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPosts(@RequestParam(name = "text", defaultValue = "") String text,
                                                 @RequestParam(name = "date_from") long dateFrom,
                                                 @RequestParam(name = "date_to") long dateTo,
                                                 @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage) {
        return new ResponseEntity<>(postService.getPosts(text,dateFrom,dateTo,offset,itemPerPage),HttpStatus.OK);
    }
    @GetMapping("/feeds")
    public ResponseEntity<PostResponse> getFeeds(@RequestParam(name = "text", defaultValue = "") String text,
                                                 @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage) {
        return new ResponseEntity<>(postService.getFeeds(text,offset,itemPerPage),HttpStatus.OK);
    }
}
