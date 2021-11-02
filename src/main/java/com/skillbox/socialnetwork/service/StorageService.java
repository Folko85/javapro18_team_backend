package com.skillbox.socialnetwork.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.platformdto.ImageDto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class StorageService {

    private final PersonRepository personRepository;

    @Value("${external.cloudinary.cloud}")
    private String cloud;

    @Value("${external.cloudinary.key}")
    private String key;

    @Value("${external.cloudinary.secret}")
    private String secret;

    public StorageService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public DataResponse uploadImage(MultipartFile image, String type, Principal principal) throws IOException {
        Person current = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        if (image.getSize() > 5242880) {
            throw new FileSizeLimitExceededException("Please reduce image" + image.getOriginalFilename(), image.getSize(), 5242880);
        }

        Cloudinary cloudinary = new Cloudinary((ObjectUtils.asMap(
                "cloud_name", cloud,
                "api_key", key,
                "api_secret", secret)));

        Map response = cloudinary.uploader().upload(image.getBytes(),
                ObjectUtils.asMap("public_id", RandomStringUtils.randomAlphabetic(10)));


        String imageUrl = (type.equals("IMAGE"))
                ? cloudinary.url()
                .transformation(new Transformation()
                        .crop("fill")
                        .width(300)
                        .height(300))
                .generate(response.get("public_id").toString())
                : response.get("url").toString();


        ImageDto imageDTO = new ImageDto()
                .setId(String.valueOf(ThreadLocalRandom.current().nextInt(100))).setUrl(imageUrl);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setTimestamp(Instant.now());
        dataResponse.setData(imageDTO);
        return dataResponse;
    }
}
