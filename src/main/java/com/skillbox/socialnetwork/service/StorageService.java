package com.skillbox.socialnetwork.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skillbox.socialnetwork.api.response.ImageDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageService {

    @Value ("${cloudinary.cloud}")
    private String cloud;

    @Value ("${cloudinary.key}")
    private String key;

    @Value ("${cloudinary.secret}")
    private String secret;

    public ImageDTO uploadImage(MultipartFile image) throws IOException {
        Cloudinary cloudinary = new Cloudinary((ObjectUtils.asMap(
                "cloud_name", cloud,
                "api_key", key,
                "api_secret", secret)));

        // проверка формата
//        if (!image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')).equals(".png")
//                && !image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')).equals(".jpg")) {
//            throw new IncorrectFormatException();
//        }
//        // проверка размера
//        if (image.getSize() > 5242880) {
//            throw new MaxUploadSizeExceededException(5242880);
//        }
//

        cloudinary.uploader().upload(image.getBytes(),
                ObjectUtils.asMap("public_id", RandomStringUtils.randomAlphabetic(12)));

        return new ImageDTO();
    }
}
