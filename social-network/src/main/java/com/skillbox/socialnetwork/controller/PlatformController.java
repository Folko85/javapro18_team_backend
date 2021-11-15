package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.platformdto.PlaceDto;
import com.skillbox.socialnetwork.api.response.platformdto.Language;
import com.skillbox.socialnetwork.service.PlatformService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.Country;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получение списка стран")
    public ListResponse<PlaceDto> getCountries(@RequestParam(required = false, defaultValue = "") String country,
                                               @RequestParam(required = false, defaultValue = "0") int offset,
                                               @RequestParam(required = false, defaultValue = "10") int itemPerPage) throws Exception {
        return platformService.getCountries(country, offset, itemPerPage);
    }

    @GetMapping("/platform/cities")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Запрос списка городов в количестве itemPerPage содержащих city")
    public ListResponse<PlaceDto> getCities(@RequestParam int countryId,
                                    @RequestParam(required = false, defaultValue = "") String city,
                                    @RequestParam(required = false, defaultValue = "0") int offset,
                                    @RequestParam(required = false, defaultValue = "10") int itemPerPage
    ) throws ClientException, ApiException {
        return platformService.getCities(countryId, city, offset, itemPerPage);
    }

    @GetMapping("/platform/languages")
    public ListResponse<Language> getLanguages() {
        return platformService.getLanguages();
    }
}
