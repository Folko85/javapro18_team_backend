package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ImageDTO;
import com.skillbox.socialnetwork.service.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ImageDTO uploadImage(){
        return storageService.uploadImage();
    }

}
