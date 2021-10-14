package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.CityDTO;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.service.PlatformService;
import com.vk.api.sdk.objects.base.Country;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Контроллер для получения списка стран и подгрузки городов для страны")
@RequestMapping("/api/v1")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }


    @GetMapping("/platform/countries")
    @Operation(summary = "Получение списка стран")
    List<Country> getCountries() throws Exception {
        return platformService.getCountries();
    }

    @GetMapping("/platform/cities")
    @Operation (summary = "Запрос списка городов в количестве itemPerPage содержащих city")
    List<CityDTO> getCities(@RequestParam int countryId) throws Exception {
        return platformService.getCities(countryId);
    }
    /*
        TODO: Это заглушка
    */
    @GetMapping("/platform/languages")
    public ResponseEntity<ListResponse> getLanguages(){
        return  new ResponseEntity<>(platformService.getLanguages(), HttpStatus.OK);
    }
}
