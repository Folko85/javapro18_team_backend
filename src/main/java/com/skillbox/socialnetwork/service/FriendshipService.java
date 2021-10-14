package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.ResponseFriendsList;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.FriendshipStatusRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Service
public class FriendshipService {
    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;
    private final PersonService personService;
    private final FriendshipStatusRepository friendshipStatusRepository;

    public FriendshipService(PersonRepository personRepository, FriendshipRepository friendshipRepository,
                             PersonService personService, FriendshipStatusRepository friendshipStatusRepository) {
        this.personRepository = personRepository;
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.friendshipStatusRepository = friendshipStatusRepository;
    }

    public ListResponse getFriends(String name, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> pageablePersonList = personRepository.findPersonByFriendship(name, person.getId(), pageable);
        return getPersonResponse(offset, itemPerPage, pageablePersonList);
    }

    private ListResponse getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        ListResponse postResponse = new ListResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(getPerson4Response(pageablePersonList.toList()));
        return postResponse;
    }

    private List<Dto> getPerson4Response(List<Person> persons) {
        List<Dto> personDataList = new ArrayList<>();
        persons.forEach(person -> {
            AuthData personData = setAuthData(person);
            personDataList.add(personData);
        });
        return personDataList;
    }


    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    public FriendsResponse200 stopBeingFriendsById(int id, Principal principal) {
        FriendsResponse200 response = new FriendsResponse200();
        response.setTimestamp(LocalDateTime.now());

        Person srcPerson = personService.findPersonByEmail(principal.getName());
        int srcId = srcPerson.getId();

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcId, id);

        if (optionalFriendship.isPresent()) {
            Friendship friendship = optionalFriendship.get();
            int statusId = friendship.getStatus().getId();

            FriendshipStatus friendshipStatus = friendshipStatusRepository.findById(statusId).get();
            friendshipStatus.setCode(FriendshipStatusCode.SUBSCRIBED);

            friendship.setStatus(friendshipStatus);

            friendshipStatusRepository.save(friendshipStatus);
            friendshipRepository.save(friendship);

            response.setError("Successfully");
            response.setMessage("Stop being friends");
        } else {
            response.setError("Unsuccessfully");
            response.setMessage("Don't stop being friends");
        }
        return response;
    }

    public FriendsResponse200 addNewFriend(int id, Principal principal) {

        //1. Формируем ответ
        FriendsResponse200 addFriendResponse = new FriendsResponse200();
        addFriendResponse.setError("Successfully");
        addFriendResponse.setTimestamp(LocalDateTime.now());
        addFriendResponse.setMessage("Adding to friends");

        //2. Находим в БД пользователя который добавляет
        Person srcPerson = personRepository.findByEMail(principal.getName()).get();
        int srcPersonId = srcPerson.getId();

        //3. Находим в БД пользователя которого добавляют
        Person dstPerson = personService.findPersonById(id).get();

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcPersonId, id);


        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcPersonId, id);

        if (friendshipOptional.isPresent()) {
            FriendshipStatus friendshipStatusById = friendshipStatusRepository.findById(friendshipOptional.get().getId()).get();
            friendshipStatusById.setTime(LocalDateTime.now());
            friendshipStatusById.setCode(FriendshipStatusCode.FRIEND);

            friendshipStatusRepository.save(friendshipStatusById);
        } else {

            //4. Создаем статус дружбы
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setCode(FriendshipStatusCode.SUBSCRIBED);

            FriendshipStatus saveFriendshipStatus = friendshipStatusRepository.save(friendshipStatus);

            //5. Создаем дружбу
            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(saveFriendshipStatus);
            newFriendship.setSrcPerson(srcPerson);
            newFriendship.setDstPerson(dstPerson);

            //6. Сохраняем в БД
            friendshipRepository.save(newFriendship);
        }
        return addFriendResponse;
    }

    public ResponseFriendsList isPersonsFriends(IsFriends isFriends, Principal principal) {
//        int id = isFriends.getUserIds().get(0);
//        int idPerson = personRepository.findByEMail(principal.getName()).get().getId();
//        List<StatusFriend> f = friendshipRepository.isMyFriend(idPerson, id);
//
//        for (StatusFriend statusFriend : f) {
//            System.out.println(statusFriend.getUserId() + " " + statusFriend.getFriendshipStatusCode());
//        }


//        StatusFriend statusFriend = new StatusFriend();
//        statusFriend.setUserId(f.getDstPerson().getId());
//        statusFriend.setFriendshipStatusCode(f.getStatus().getCode());
//
//        List<StatusFriend> statusList = new ArrayList<>();
//        statusList.add(statusFriend);
//
        ResponseFriendsList responseFriendsList = new ResponseFriendsList();
//        responseFriendsList.setData(statusList);

        return responseFriendsList;
    }
}
