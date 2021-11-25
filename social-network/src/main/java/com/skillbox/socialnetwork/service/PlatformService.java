package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.platformdto.LanguageDto;
import com.skillbox.socialnetwork.api.response.platformdto.PlaceDto;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.database.responses.GetCitiesResponse;
import com.vk.api.sdk.objects.database.responses.GetCountriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlatformService {

    @Value("${external.vk.id}")
    private String id;

    @Value("${external.vk.token}")
    private String token;


    @Cacheable ("countries")
    public ListResponse<PlaceDto> getCountries(String country, int offset, int itemPerPage) {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        GetCountriesResponse response;
        List<PlaceDto> countries = new ArrayList<>();
        try {
            response = vk.database().getCountries(actor).needAll(true)
                    .offset(offset).count(itemPerPage).lang(Lang.RU).execute();
            countries = response.getItems().stream()
                    .map(c -> new PlaceDto().setId(c.getId()).setTitle(c.getTitle()))
                    .filter(c -> c.getTitle().toLowerCase().startsWith(country.toLowerCase())).filter(x -> x.getId() != 0).collect(Collectors.toList());
        } catch (ApiException | ClientException e) {
            log.warn(Arrays.toString(e.getStackTrace()));
        }
        log.info("We get that countries with {} only one time at day", country);
        ListResponse<PlaceDto> result = new ListResponse<>();
        result.setTotal(countries.size());
        result.setTimestamp(Instant.now());
        result.setPerPage(itemPerPage);
        result.setData(countries);
        return result;
    }

    @Cacheable ("cities")
    public ListResponse<PlaceDto> getCities(int countryId, String city, int offset, int itemPerPage) {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        GetCitiesResponse response = null;
        List<PlaceDto> cities = new ArrayList<>();
        ListResponse<PlaceDto> result = new ListResponse<>();
        try {
            response = vk.database().getCities(actor, countryId).needAll(true)
                    .q(city).offset(offset).count(itemPerPage).lang(Lang.RU).execute();
            cities = response.getItems().stream()
                    .map(x -> new PlaceDto().setId(x.getId()).setTitle(x.getTitle())).filter(x -> x.getId() != 0).collect(Collectors.toList());
        } catch (ApiException | ClientException e) {
            log.warn(Arrays.toString(e.getStackTrace()));
        }
        log.info("We get that cities with {} only one time at day", city);
        result.setTotal((response != null) ? response.getCount() : 0);
        result.setTimestamp(Instant.now());
        result.setPerPage(itemPerPage);
        result.setData(cities);
        return result;
    }

    public ListResponse<LanguageDto> getLanguages() {
        ListResponse<LanguageDto> listResponse = new ListResponse<>();
        listResponse.setTimestamp(Instant.now());
        listResponse.setTotal(1);
        listResponse.setOffset(0);
        listResponse.setPerPage(1);
        LanguageDto language = new LanguageDto();
        language.setId(1);
        language.setTitle("Русский");
        List<LanguageDto> languages = new ArrayList<>();
        languages.add(language);
        listResponse.setData(languages);
        return listResponse;
    }
}
