package com.skillbox.socialnetwork.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.platformdto.ImageDto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.PostFile;
import com.skillbox.socialnetwork.exception.ApiConnectException;
import com.skillbox.socialnetwork.repository.FileRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StorageService {

    private final PersonRepository personRepository;

    private final FileRepository fileRepository;

    @Value("${external.cloudinary.cloud}")
    private String cloud;

    @Value("${external.cloudinary.key}")
    private String key;

    @Value("${external.cloudinary.secret}")
    private String secret;

    public StorageService(PersonRepository personRepository, FileRepository fileRepository) {
        this.personRepository = personRepository;
        this.fileRepository = fileRepository;
    }

    public DataResponse<ImageDto> uploadImage(MultipartFile image, String type, Principal principal) throws IOException {
        Person current = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        if (image.getSize() > 5242880) {
            throw new FileSizeLimitExceededException("Please reduce image" + image.getOriginalFilename(), image.getSize(), 5242880);
        }

        Cloudinary cloudinary = getInstance();

        Map response = cloudinary.uploader().upload(image.getBytes(),
                ObjectUtils.asMap("public_id", RandomStringUtils.randomAlphabetic(10)));


        ImageDto imageDTO = new ImageDto();
        if (type.equals("IMAGE")) {
            String url = cloudinary.url()
                    .transformation(new Transformation()
                            .crop("fill")
                            .width(300)
                            .height(300))
                    .format("jpg")
                    .generate(response.get("public_id").toString());
            PostFile postFile = fileRepository.save(new PostFile().setUrl(url).setUserId(current.getId()));
            imageDTO.setId(String.valueOf(postFile.getId())).setUrl(url);


        } else {
            String url = response.get("url").toString();
            PostFile postFile = fileRepository.save(new PostFile().setUrl(url).setUserId(current.getId()));
            imageDTO.setId(String.valueOf(postFile.getId())).setUrl(url);
        }

        DataResponse<ImageDto> dataResponse = new DataResponse<>();
        dataResponse.setTimestamp(Instant.now());
        dataResponse.setData(imageDTO);
        return dataResponse;
    }

    public Cloudinary getInstance() {
        return new Cloudinary((ObjectUtils.asMap(
                "cloud_name", cloud,
                "api_key", key,
                "api_secret", secret)));
    }

    public AccountResponse deleteImage(int id) throws ApiConnectException {
        String url = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Нет такого файла")).getUrl();
        deleteImageByUrl(url);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setTimestamp(ZonedDateTime.now().toInstant());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "file successfully deleted");
        accountResponse.setData(dateMap);
        return accountResponse;
    }

    public void deleteImageByUrl(String url) throws ApiConnectException {
        try {
            String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            Cloudinary cloudinary = getInstance();
            Map response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            fileRepository.delete(fileRepository.findByUrl(url));
        } catch (IOException ex) {
            log.error("Ошибка при удалении файла");
            throw new ApiConnectException("Не удалось удалить файл");
        }

    }
}
