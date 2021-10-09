package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.SearchUser;
import com.skillbox.socialnetwork.api.response.postdto.Dto;
import com.skillbox.socialnetwork.api.response.postdto.PostResponse;
import com.skillbox.socialnetwork.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class ProfileController {

    private final PersonService personService;

    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/api/v1/users/search")
    @PreAuthorize("hasAuthority('user:write')")
    public @ResponseBody
    ResponseEntity<?> search(@RequestBody SearchUser searchUser) {
        List<Dto> searchPerson = personService.searchPerson(searchUser);

        PostResponse list = new PostResponse();
        list.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        list.setTotal(searchPerson.size());
        list.setOffset(1);
        list.setError("Successfully");
        list.setPerPage(searchUser.getItemPerPage());
        list.setData(searchPerson);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
