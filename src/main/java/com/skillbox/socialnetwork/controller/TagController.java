package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.tagdto.ManyTagsResponse;
import com.skillbox.socialnetwork.entity.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tags/")
public class TagController {

    @GetMapping
    public ManyTagsResponse getTags(@RequestParam(required = false) String tag, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer itemPerPage){
        return new ManyTagsResponse();
    }

    @PostMapping
    public ManyTagsResponse postTag(@RequestBody Tag tag){
        return new ManyTagsResponse();
    }

    @DeleteMapping
    public ManyTagsResponse deleteTag(@RequestParam Integer id){
        return new ManyTagsResponse();
    }
}
