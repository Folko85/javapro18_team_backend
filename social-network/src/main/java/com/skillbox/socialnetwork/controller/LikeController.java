package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LikeRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.likedto.LikeData;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
import com.skillbox.socialnetwork.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@Tag(name = "Контроллер для работы с лайками")
@RequestMapping("/api/v1/")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/likes")
    @Operation(summary = "Поставить лайк", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<LikeData> putLikes(@RequestBody LikeRequest likeRequest,
                                           Principal principal) throws LikeNotFoundException {
        return likeService.putLikes(likeRequest, principal);
    }

    @DeleteMapping("/likes")
    @Operation(summary = "Удалить лайк", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<LikeData> deleteLikes(@RequestParam(name = "item_id") int itemId,
                                              @RequestParam(name = "type") String type,
                                              Principal principal) throws LikeNotFoundException {
        return likeService.deleteLike(itemId, type, principal);
    }

    @GetMapping("/likes")
    @Operation(summary = "Получить лайки", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<LikeData> getLikes(@RequestParam(name = "item_id") int itemId,
                                           @RequestParam(name = "type") String type) {
        return likeService.getLikes(itemId, type);
    }
}
