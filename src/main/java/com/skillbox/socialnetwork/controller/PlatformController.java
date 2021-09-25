package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.service.PlatformService;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.database.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }


    @GetMapping("/platform/countries")
    List<Country> getCountries() throws Exception {
        return platformService.getCountries();
    }

    @GetMapping("/platform/cities")
    List<City> getCities(@RequestParam int countryId, @RequestParam String city, @RequestParam int itemPerPage) throws Exception {
        return platformService.getCities(countryId, city, itemPerPage);
    }
}
