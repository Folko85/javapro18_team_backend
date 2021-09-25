package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.CountryDTO;
import com.skillbox.socialnetwork.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }


    @GetMapping("/platform/countries")
    ResponseEntity<CountryDTO[]> getCountries() {
        return platformService.getCountries();
    }
}
