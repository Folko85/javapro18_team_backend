package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class DialogService {
        private final PersonRepository personRepository;

    public DialogService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public ListResponse getDialogs(String text, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> pageablePostList = postRepository.findPostsByTextContaining(text, pageable);
        return getPostResponse(offset, itemPerPage, pageablePostList, person);
    }
    private Person findPerson(String eMail) {
        return personRepository.findPersonByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
