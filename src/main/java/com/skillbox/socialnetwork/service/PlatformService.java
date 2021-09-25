package com.skillbox.socialnetwork.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.database.responses.GetCountriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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



        UserActor actor = new UserActor(Integer.valueOf(id),token);

        GetCountriesResponse response = vk.database().getCountries(actor).needAll(true).execute();
        return response.getItems();

    }

}
