package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.platformdto.CityDto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.platformdto.Language;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.database.responses.GetCitiesResponse;
import com.vk.api.sdk.objects.database.responses.GetCountriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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


    public List<Country> getCountries() throws Exception {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        GetCountriesResponse response = vk.database().getCountries(actor).needAll(true).count(250).lang(Lang.RU).execute();
        return response.getItems();

    }

    public List<CityDto> getCities(int countryId) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);

        GetCitiesResponse response = vk.database().getCities(actor, countryId).lang(Lang.RU).execute();
        return response.getItems().stream().map(x -> {
            CityDto result = new CityDto();
            BeanUtils.copyProperties(x, result);
            return result;
        }).collect(Collectors.toList());
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
        List<Language> languages= new ArrayList<>();
        languages.add(language);
        listResponse.setData(languages);
        return listResponse;
    }
}
