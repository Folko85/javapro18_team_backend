package com.skillbox.socialnetwork.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skillbox.socialnetwork.api.response.ImageDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    @Value ("${cloudinary.cloud}")
    private String cloud;

    @Value ("${cloudinary.key}")
    private String key;

    @Value ("${cloudinary.secret}")
    private String secret;

    public StorageService(Cloudinary cloudinary) {

    }


    public ImageDTO uploadImage(MultipartFile image) {
        Cloudinary cloudinary = new Cloudinary((ObjectUtils.asMap(
                "cloud_name", cloud,
                "api_key", key,
                "api_secret", secret)));

//        StringBuilder sb = new StringBuilder(LENGTH);
//        for (int i = 0; i < LENGTH; i++) {
//            int index = (int) (SYMBOLS.length() * Math.random());
//            sb.append(SYMBOLS.charAt(index));
//        }
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
//        String path = UPLOAD + sb.substring(0, 4) + "/" + sb.substring(4, 8) + "/" + sb.substring(8, 12);
//        path = path + "/" + sb.substring(12, 16);
//
//        return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
//                "public_id", path)).get("url").toString();
        return new ImageDTO();
    }
}
