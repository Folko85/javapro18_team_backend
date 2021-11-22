package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsDto;
import com.skillbox.socialnetwork.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Tag(name = "Профиль", description = "Работа с профилем и с публичной информацией пользователя")
public class ProfileController {

    private final PersonService personService;

    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Поиск пользователя", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/users/search")
    @PreAuthorize("hasAuthority('user:write')")
    public @ResponseBody
    ResponseEntity<ListResponse<FriendsDto>> search(@RequestParam(name = "first_name", defaultValue = "") String firstName,
                             @RequestParam(name = "last_name", defaultValue = "") String lastName,
                             @RequestParam(name = "age_from", defaultValue = "-1") int ageFrom,
                             @RequestParam(name = "age_to", defaultValue = "-1") int ageTo,
                             @RequestParam(name = "country", defaultValue = "") String country,
                             @RequestParam(name = "city", defaultValue = "") String city,
                             @RequestParam(name = "offset", defaultValue = "0") int offset,
                             @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                             Principal principal) {

        ListResponse<FriendsDto> listResponse = personService.searchPerson(firstName, lastName, ageFrom, ageTo,
                country, city, offset, itemPerPage, principal);

        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

}
