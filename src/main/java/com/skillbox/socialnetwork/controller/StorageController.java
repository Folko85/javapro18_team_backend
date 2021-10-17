package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.service.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping ("/api/v1/storage")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String type, Principal principal) throws IOException {
        return storageService.uploadImage(file, principal);
    }

}
