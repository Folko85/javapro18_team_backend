package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final PersonService personService;

    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/api/v1/users/search")
    @PreAuthorize("hasAuthority('user:write')")
    public @ResponseBody
    ResponseEntity<?> search(@RequestParam(name = "first_name") String firstName,
                             @RequestParam(name = "last_name", defaultValue = "") String lastName,
                             @RequestParam(name = "age_from", defaultValue = "0") int ageFrom,
                             @RequestParam(name = "age_to", defaultValue = "120") int ageTo,
                             @RequestParam(name = "country_id", defaultValue = "1") int countryId,
                             @RequestParam(name = "city_id", defaultValue = "1") int cityId,
                             @RequestParam(name = "offset", defaultValue = "0") int offset,
                             @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage) {

        ListResponse listResponse = personService.searchPerson(firstName, lastName, ageFrom, ageTo, countryId, cityId, offset, itemPerPage);

        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

}
