package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.service.FriendshipService;
import com.skillbox.socialnetwork.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class FriendshipController {


    private final FriendshipRepository friendshipRepository;
    private final PersonService personService;
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipRepository friendshipRepository, PersonService personService, FriendshipService friendshipService) {
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                                   @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                   @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                   Principal principal) {
        return new ResponseEntity<>(friendshipService.getFriends(name, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> stopBeingFriends(@PathVariable int id) {

        Optional<Friendship> friendship = friendshipService.findMyFriendshipByIdMyFriend(id);

        if (friendship.isPresent()) {
            friendshipService.stopBeingFriendsById(id);

            FriendsResponse200 response = new FriendsResponse200();
            response.setError("Successfully");
            response.setTimestamp(LocalDateTime.now());
            response.setMessage("Stop being friends");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/api/v1/friends{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> addingToFriends(@PathVariable int id, Principal principal) {

        //1. Находи в БД пользователя ктороый добавляет
        String eMailSrcPerson = principal.getName();
        Person srcPerson = personService.findPersonByEmail(eMailSrcPerson);

        //2. Находим в БД пользователя которого добавляют
        Optional<Person> optionalPerson = personService.findPersonById(id);

        //3. Если пользователь с таки id существует и у srcPerson его нет в друзьях выполняем код
        if (optionalPerson.isPresent() && friendshipService.findMyFriendshipByIdMyFriend(id).isEmpty()) {

            Person dstPerson = optionalPerson.get();

            //4. Создаем статус дружбы
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setCode(FriendshipStatusCode.FRIEND);

            //5. Создаем дружбу
            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(friendshipStatus);
            newFriendship.setSrcPerson(srcPerson);
            newFriendship.setDstPerson(dstPerson);

            //6. Сохраняем в БД
            friendshipService.save(newFriendship);

            //7. Формируем ответ
            FriendsResponse200 addFriendResponse = new FriendsResponse200();
            addFriendResponse.setError("Successfully");
            addFriendResponse.setTimestamp(LocalDateTime.now());
            addFriendResponse.setMessage("Adding to friends");

            return new ResponseEntity<>(addFriendResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
