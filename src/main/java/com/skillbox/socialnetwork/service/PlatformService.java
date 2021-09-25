package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.CityDTO;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.database.City;
import com.vk.api.sdk.objects.database.responses.GetCitiesResponse;
import com.vk.api.sdk.objects.database.responses.GetCountriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlatformService {

    @Value("${vk.id}")
    private String id;

    @Value("${vk.token}")
    private String token;


    public List<Country> getCountries() throws Exception {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        GetCountriesResponse response = vk.database().getCountries(actor).needAll(true).lang(Lang.RU).execute();
        return response.getItems();

    }

    public List<CityDTO> getCities(int countryId, String city, int count) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(Integer.valueOf(id), token);
        if (count == 0) {
            count = 10;
        }

        if (city == null || city.isEmpty()) {
            throw new ApiException("Need more 0 letters");
        }

        GetCitiesResponse response = vk.database().getCities(actor, countryId).count(count).q(city).needAll(true).lang(Lang.RU).execute();
        return response.getItems().stream().map(x -> {
            CityDTO result = new CityDTO();
            BeanUtils.copyProperties(x, result);
            return result;
        }).collect(Collectors.toList());
    }
}
