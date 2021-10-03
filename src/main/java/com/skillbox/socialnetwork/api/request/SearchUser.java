package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchUser {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("age_from")
    private int ageFrom;
    @JsonProperty("age_to")
    private int ageTo;
    @JsonProperty("country_id")
    private int countryId;
    @JsonProperty("city_id")
    private int cityId;
    private int offset;
    private int itemPerPage;
}
