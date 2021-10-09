package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.GetFriendsListRequest;
import com.skillbox.socialnetwork.api.response.friendsDTO.FriendsDto;
import com.skillbox.socialnetwork.api.response.postDTO.Dto;
import com.skillbox.socialnetwork.api.response.postDTO.PostResponse;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private final PersonService personService;
    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, PersonService personService,
                             PersonRepository personRepository) {
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.personRepository = personRepository;
    }

    public Optional<Friendship> findMyFriendshipByIdMyFriend(Integer id) {
        return friendshipRepository.findById(id);
    }

    public List<Person> findMyFriendByName(String firstName, int itemPerPage) {
        Pageable pageable = PageRequest.ofSize(itemPerPage);

        List<Friendship> friendshipList = friendshipRepository
                .findBySrcPersonFirstNameOrDstPersonFirstName(firstName, pageable);

        if (friendshipList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return friendshipList.stream().map(friendship -> {
                if (friendship.getSrcPerson().getFirstName().equals(firstName)) {
                    return friendship.getSrcPerson();
                } else {
                    return friendship.getDstPerson();
                }
            }).collect(Collectors.toList());
        }
    }

    public void stopBeingFriendsById(int id) {
        Optional<Friendship> friendshipOptional = friendshipRepository.findById(id);

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            friendship.getStatus().setCode(FriendshipStatusCode.SUBSCRIBED);

            friendshipRepository.save(friendship);
        }
    }

    public Friendship save(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    public PostResponse findMyFriends(GetFriendsListRequest getFriendsListRequest,
                                      Principal principal) {

        PostResponse response = new PostResponse();
        response.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        response.setOffset(0);
        response.setError("Successfully");
        response.setPerPage(20);

        if (getFriendsListRequest.getName().length() == 0) {
            String email = principal.getName();
            List<Friendship> friendships = friendshipRepository.getAllFriends(email);

            List<Dto> friends = friendships
                    .stream()
                    .map(friendship -> {
                        if (friendship.getSrcPerson().getEMail().equals(email)) {
                            return personService.friendsToPojo(friendship.getDstPerson());
                        } else {
                            return personService.friendsToPojo(friendship.getSrcPerson());
                        }
                    })
                    .collect(Collectors.toList());

            response.setTotal(friends.size());
            response.setData(friends);
            return response;

        } else {

            String friendsName = getFriendsListRequest.getName();
            int itemPerPage = getFriendsListRequest.getItemPerPage();

            List<Person> myFriends = findMyFriendByName(friendsName, itemPerPage);

            if (myFriends.size() > 0) {
                List<Dto> friendsList = myFriends
                        .stream()
                        .map(personService::friendsToPojo)
                        .collect(Collectors.toList());

                response.setTotal(myFriends.size());
                response.setData(friendsList);
                return response;
            } else {
                response.setData(new ArrayList<>());
                return response;
            }
        }
    }
}
