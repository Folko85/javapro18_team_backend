package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.CountryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PlatformService {

    public ResponseEntity<CountryDTO[]> getCountries() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity("http://api.vk.com/method/database.getCountries?v=5.81&need_all=1&access_token=6a54fa386a54fa386a54fa38f06a3d0ed966a546a54fa3836e63c76572f39c9753d8adb", CountryDTO[].class);
    }
}
