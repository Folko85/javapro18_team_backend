package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse getTags(@RequestParam(required = false, defaultValue = "") String tag,
                                @RequestParam(required = false, defaultValue = "0") Integer offset,
                                @RequestParam(required = false, defaultValue = "10") Integer itemPerPage){
        return tagService.getTags(tag, offset, itemPerPage);
    }

    @PostMapping
    @Operation (summary = "Добавить тег")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse postTag(@RequestBody TagDto tag){
        return tagService.postTag(tag);
    }

    @DeleteMapping
    @Operation (summary = "Удалить тег")
    @PreAuthorize("hasAuthority('user:write')")
    public AccountResponse deleteTag(@RequestParam int id){
        return tagService.deleteTag(id);
    }
}
