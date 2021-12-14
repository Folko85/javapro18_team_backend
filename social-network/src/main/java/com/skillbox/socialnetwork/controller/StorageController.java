package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.platformdto.ImageDto;
import com.skillbox.socialnetwork.exception.ApiConnectException;
import com.skillbox.socialnetwork.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@Tag(name = "Контроллер для загрузки файлов")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/api/v1/storage")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Загрузка изображения", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<ImageDto> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String type, Principal principal) throws IOException {
        return storageService.uploadImage(file, type, principal);
    }

    @DeleteMapping("/api/v1/storage/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Удаление изображения", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<SuccessResponse> deleteImage(@PathVariable int id) throws ApiConnectException {
        return storageService.deleteImage(id);
    }

}
