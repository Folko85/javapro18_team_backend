package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostServiceImpl post;

    @Autowired
    public PostController(PostServiceImpl post) {
        this.post = post;
    }


    @GetMapping()
    public ResponseEntity<List<Post>> getPostsByText(@RequestParam String text, LocalDateTime dateFrom, LocalDateTime dateTo, int offset) {
        return ResponseEntity.ok().body(post.getByText(text, dateFrom, dateTo, offset));
    }
}
