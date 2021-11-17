package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation (summary = "Получить теги", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<TagDto> getTags(@RequestParam(required = false, defaultValue = "") String tag,
                                @RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false, defaultValue = "10") int itemPerPage){
        return tagService.getTags(tag, offset, itemPerPage);
    }

    @PostMapping
    @Operation (summary = "Добавить тег", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<TagDto> postTag(@RequestBody TagDto tag){
        return tagService.postTag(tag);
    }

    @DeleteMapping
    @Operation (summary = "Удалить тег", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:administrate')")
    public AccountResponse deleteTag(@RequestParam Integer id){
        return tagService.deleteTag(id);
    }
}
