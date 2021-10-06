package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.tagdto.DeleteTagResponse;
import com.skillbox.socialnetwork.api.response.tagdto.ManyTagsResponse;
import com.skillbox.socialnetwork.api.response.tagdto.OneTagResponse;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер для работы с тегами")
@RequestMapping("/api/v1/tags/")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation (summary = "Получить теги")
    public ManyTagsResponse getTags(@RequestParam(required = false) String tag, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer itemPerPage){
        return tagService.getTags(tag, offset, itemPerPage);
    }

    @PostMapping
    @Operation (summary = "Добавить тег")
    public OneTagResponse postTag(@RequestBody Tag tag){
        return tagService.postTag(tag);
    }

    @DeleteMapping
    @Operation (summary = "Удалить тег")
    public DeleteTagResponse deleteTag(@RequestParam int id){
        return tagService.deleteTag(id);
    }
}
