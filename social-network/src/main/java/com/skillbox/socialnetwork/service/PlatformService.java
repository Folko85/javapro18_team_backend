package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.platformdto.Language;
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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlatformService {

    @Value("${external.vk.id}")
    private String id;

    @Value("${external.vk.token}")
    private String token;


    public ListResponse<PlaceDto> getCountries(String country, int offset, int itemPerPage) throws Exception {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        GetCountriesResponse response = vk.database().getCountries(actor).needAll(true)
                .code(country).offset(offset).count(itemPerPage).lang(Lang.RU).execute();
        List<PlaceDto> countries = response.getItems().stream()
                .map(c -> new PlaceDto().setId(c.getId()).setTitle(c.getTitle())).collect(Collectors.toList());
        ListResponse<PlaceDto> result = new ListResponse<>();
        result.setTotal(response.getCount());
        result.setTimestamp(Instant.now());
        result.setPerPage(itemPerPage);
        result.setData(countries);
        return result;
    }

    public ListResponse<PlaceDto> getCities(int countryId, String city, int offset, int itemPerPage) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);

        GetCitiesResponse response = vk.database().getCities(actor, countryId).needAll(true)
                .q(city).offset(offset).count(itemPerPage).lang(Lang.RU).execute();
        List<PlaceDto> cities = response.getItems().stream()
                .map(x -> new PlaceDto().setId(x.getId()).setTitle(x.getTitle())).collect(Collectors.toList());
        ListResponse<PlaceDto> result = new ListResponse<>();
        result.setTotal(response.getCount());
        result.setTimestamp(Instant.now());
        result.setPerPage(itemPerPage);
        result.setData(cities);
        return result;
    }

    public ListResponse<Language> getLanguages() {
        ListResponse<Language> listResponse = new ListResponse<>();
        listResponse.setTimestamp(Instant.now());
        listResponse.setTotal(1);
        listResponse.setOffset(0);
        listResponse.setPerPage(1);
        Language language = new Language();
        language.setId(1);
        language.setTitle("Русский");
        List<Language> languages = new ArrayList<>();
        languages.add(language);
        listResponse.setData(languages);
        return listResponse;
    }
}
