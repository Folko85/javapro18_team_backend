package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
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

}
