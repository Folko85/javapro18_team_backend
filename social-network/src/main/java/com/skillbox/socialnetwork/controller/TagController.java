package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.postdto.TagDto;
import com.skillbox.socialnetwork.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с тегами.
 */
@RestController
@Tag(name = "Контроллер для работы с тегами")
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Получить теги.
     *
     * @param tag
     * @param offset
     * @param itemPerPage
     * @return
     */
    @GetMapping
    @Operation(summary = "Получить теги", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<TagDto> getTags(@RequestParam(required = false, defaultValue = "") String tag,
                                        @RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int itemPerPage) {
        return tagService.getTags(tag, offset, itemPerPage);
    }

    /**
     * Добавить тег.
     *
     * @param tag
     * @return
     */
    @PostMapping
    @Operation(summary = "Добавить тег", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<TagDto> postTag(@RequestBody TagDto tag) {
        return tagService.postTag(tag);
    }

    /**
     * Удалить тег.
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "Удалить тег", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> deleteTag(@RequestParam Integer id) {
        return tagService.deleteTag(id);
    }
}
