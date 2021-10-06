package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.tagdto.DeleteTagResponse;
import com.skillbox.socialnetwork.api.response.tagdto.ManyTagsResponse;
import com.skillbox.socialnetwork.api.response.tagdto.OneTagResponse;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.service.TagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tags/")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ManyTagsResponse getTags(@RequestParam(required = false) String tag, @RequestParam(required = false) int offset, @RequestParam(required = false) int itemPerPage){
        return tagService.getTags(tag, offset, itemPerPage);
    }

    @PostMapping
    public OneTagResponse postTag(@RequestBody Tag tag){
        return tagService.postTag(tag);
    }

    @DeleteMapping
    public DeleteTagResponse deleteTag(@RequestParam int id){
        return tagService.deleteTag(id);
    }
}
