package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.ResponseFriendsList;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.StatusFriend;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import com.skillbox.socialnetwork.exception.*;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.FriendshipStatusRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static com.skillbox.socialnetwork.service.AuthService.setDeletedAuthData;
import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
public class FriendshipService {
    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;
    private final PersonService personService;
    private final FriendshipStatusRepository friendshipStatusRepository;
    private final NotificationService notificationService;

    public FriendshipService(PersonRepository personRepository, FriendshipRepository friendshipRepository,
                             PersonService personService, FriendshipStatusRepository friendshipStatusRepository,
                             NotificationService notificationService) {
        this.personRepository = personRepository;
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.friendshipStatusRepository = friendshipStatusRepository;
        this.notificationService = notificationService;
    }

    public ListResponse<AuthData> getFriends(String name, int offset, int itemPerPage, Principal principal) {
        log.debug("метод получения друзей");
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> friendsPage = personRepository.findFriends(name, person.getId(), pageable);
        return getPersonResponse(offset, itemPerPage, friendsPage);
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    private Person findPerson(int id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("" + id));
    }

    public FriendsResponse200 stopBeingFriendsById(int id, Principal principal) {
        log.debug("метод удаления из друзей");

        Person srcPerson = personService.findPersonByEmail(principal.getName());

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson.getId(), id);

        if (optionalFriendship.isPresent()) {
            Friendship friendship = optionalFriendship.get();
            int statusId = friendship.getStatus().getId();

            FriendshipStatus friendshipStatus = friendshipStatusRepository
                    .findById(statusId).orElseThrow(() -> new UsernameNotFoundException("friendship status not found"));
            friendshipStatus.setCode(FriendshipStatusCode.SUBSCRIBED);

            friendship.setStatus(friendshipStatus);

            friendshipStatusRepository.save(friendshipStatus);
            friendshipRepository.save(friendship);

            return getFriendResponse200("Successfully", "Stop being friends");
        } else {
            return getFriendResponse200("Unsuccessfully", "Don't stop being friends");
        }
    }

    public FriendsResponse200 addNewFriend(int id, Principal principal) throws DeletedAccountException, AddingOrSubcribingOnBlockerPersonException, AddingOrSubcribingOnBlockedPersonException, AddingYourselfToFriends {
        log.debug("метод добавления в друзья");


        Person srcPerson = personRepository
                .findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("person not found"));
        int srcPersonId = srcPerson.getId();

        Person dstPerson = personService.findPersonById(id).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        if (srcPerson.getId() == id) {
            throw new AddingYourselfToFriends("Нельзя добавить себя в друзья");
        }

        if (dstPerson.isDeleted()) {
            throw new DeletedAccountException("This Account was deleted");
        }
        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcPersonId, id);


        if (isBlockedBy(dstPerson.getId(), srcPerson.getId(), friendshipOptional)) {
            throw new AddingOrSubcribingOnBlockerPersonException("This Person Blocked You");
        }
        if (isBlockedBy(srcPerson.getId(), dstPerson.getId(), friendshipOptional)) {
            throw new AddingOrSubcribingOnBlockedPersonException("You Blocked this Person");
        }

        /*
          Src добавляет Dst
          Если статуса нет, то создается с татус с кодом REQUEST
          Если статус есть и он REQUEST, то меняем его на FRIEND
         */

        if (friendshipOptional.isPresent() &&
                friendshipOptional.get().getStatus().getCode().equals(FriendshipStatusCode.REQUEST) &&
                friendshipOptional.get().getSrcPerson().getId() == id) {


            FriendshipStatus friendshipStatusById = friendshipOptional.get().getStatus();

            friendshipStatusById.setTime(LocalDateTime.now());
            friendshipStatusById.setCode(FriendshipStatusCode.FRIEND);

            friendshipStatusRepository.save(friendshipStatusById);
        } else if (friendshipOptional.isEmpty()) {
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setCode(FriendshipStatusCode.REQUEST);

            FriendshipStatus saveFriendshipStatus = friendshipStatusRepository.save(friendshipStatus);

            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(saveFriendshipStatus);
            newFriendship.setSrcPerson(srcPerson);
            newFriendship.setDstPerson(dstPerson);

            friendshipRepository.save(newFriendship);
            //Notification
            notificationService.createNotification(newFriendship.getDstPerson(), newFriendship.getId(), NotificationType.FRIEND_REQUEST);
            sendNotification(newFriendship);
            //Notification
        } else {
            throw new AddingYourselfToFriends("жди подтверждения");
        }
        return getFriendResponse200("Successfully", "Adding to friends");
    }

    public ListResponse<AuthData> getFriendsRequests(String name, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> personByStatusCode = personRepository
                .findPersonByStatusCode(name, person.getId(), FriendshipStatusCode.REQUEST, pageable);

        return getPersonResponse(offset, itemPerPage, personByStatusCode);
    }

    public ListResponse<AuthData> recommendedUsers(int offset, int itemPerPage, Principal principal) {
        log.debug("метод получения рекомендованных друзей");
        Person person = findPerson(principal.getName());
        log.debug("поиск рекомендованных друзей для пользователя: ".concat(person.getFirstName()));
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        LocalDate birthdayPerson;
        LocalDate startDate = null;
        LocalDate stopDate = null;
        List<Integer> blockers = friendshipRepository.findBlockersIds(person.getId());
        blockers.add(person.getId());
        if (person.getBirthday() != null) {
            //подбираем пользователей, возрост которых отличается на +-2 года
            birthdayPerson = person.getBirthday();
            startDate = birthdayPerson.minusYears(2);
            stopDate = birthdayPerson.plusYears(2);
        }
        // Получаем для подбора по городу
        String city = Strings.hasText(person.getCity()) ? person.getCity() : "";
        Page<Person> personList = personRepository.findByOptionalParametrs(
                "", "", startDate, stopDate, city, "", pageable, blockers);

        if (personList.isEmpty()) {
            pageable = PageRequest.of(0, 10);
            personList = get10Users(person.getEMail(), pageable, blockers);
        }

        if (personList.getTotalElements() < 10) {
            Pageable pageable2 = PageRequest.of(0, (int) (10 - personList.getTotalElements()));
            Page<Person> personList2 = get10Users(person.getEMail(), pageable2, blockers);

            List<Person> persons = personList.stream().collect(Collectors.toList());
            List<Person> persons2 = personList2.stream().collect(Collectors.toList());

            persons.addAll(persons2);

            personList = new PageImpl<>(persons, pageable, persons.size());
        }

        return getPersonResponse(offset, itemPerPage, personList);

    }

    public ResponseFriendsList isPersonsFriends(IsFriends isFriends, Principal principal) {
        log.debug("метод проверки являются ли переданные друзбя друзьями");
        int idPerson = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("person not found")).getId();

        List<StatusFriend> statusFriendList = new ArrayList<>();
        FriendshipStatusCode friendshipStatusCode;

        for (int friendId : isFriends.getUserIds()) {
            friendshipStatusCode = friendshipRepository
                    .isMyFriend(idPerson, friendId, FriendshipStatusCode.FRIEND).orElseThrow(() -> new UsernameNotFoundException("user not found"));

            statusFriendList.add(new StatusFriend(friendId, friendshipStatusCode));
        }

        ResponseFriendsList responseFriendsList = new ResponseFriendsList();
        responseFriendsList.setData(statusFriendList);

        return responseFriendsList;
    }

    //==========================================================================================================
    private ListResponse<AuthData> getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        ListResponse<AuthData> postResponse = new ListResponse<>();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(getPerson4Response(pageablePersonList.toList()));
        return postResponse;
    }


    private List<AuthData> getPerson4Response(List<Person> persons) {
        List<AuthData> personDataList = new ArrayList<>();
        persons.forEach(person -> {
            AuthData personData;
            if (person.isDeleted()) {
                personData = setDeletedAuthData(person);
            } else {
                personData = setAuthData(person);
            }
            personDataList.add(personData);
        });
        return personDataList;
    }

    private FriendsResponse200 getFriendResponse200(String error, String message) {
        FriendsResponse200 response = new FriendsResponse200();
        response.setTimestamp(LocalDateTime.now());
        response.setError(error);
        response.setMessage(message);
        return response;
    }

    /**
     * Src Person BlOCKED Dest Person or
     * Dest person WASBLOCKEDBY Src Person or
     * Src and Dest blocked Each other (DEADLOCK)
     */
    public AccountResponse blockUser(Principal principal, int id) throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException {
        Person current = findPerson(principal.getName());
        if (current.getId() == id) throw new UserBlocksHimSelfException();
        Person blocking = findPerson(id);
        if (blocking.isDeleted()) throw new BlockingDeletedAccountException();
        Optional<Friendship> optional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(id, current.getId());
        if (!isBlockedBy(current.getId(), blocking.getId(), optional)) {
            if (optional.isEmpty()) {
                createFriendship(current, blocking, FriendshipStatusCode.BLOCKED);
            } else {
                Friendship friendship = optional.get();
                FriendshipStatus friendshipStatus = friendship.getStatus();
                if (friendshipStatus.getCode().equals(FriendshipStatusCode.WASBLOCKEDBY)) {
                    friendshipStatus.setCode(FriendshipStatusCode.DEADLOCK);
                } else if (current.getId().equals(friendship.getSrcPerson().getId())) {
                    friendshipStatus.setCode(FriendshipStatusCode.BLOCKED);
                } else {
                    friendshipStatus.setCode(FriendshipStatusCode.WASBLOCKEDBY);
                }
                friendshipStatusRepository.save(friendshipStatus);
                friendshipRepository.save(friendship);
            }
        } else {
            throw new BlockAlreadyExistsException();

        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setTimestamp(ZonedDateTime.now().toInstant());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        accountResponse.setData(dateMap);
        return accountResponse;

    }

    public void createFriendship(Person src, Person dest, FriendshipStatusCode friendshipStatusCode) {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setCode(friendshipStatusCode);
        FriendshipStatus saveFriendshipStatus = friendshipStatusRepository.save(friendshipStatus);
        Friendship newFriendship = new Friendship();
        newFriendship.setStatus(saveFriendshipStatus);
        newFriendship.setSrcPerson(src);
        newFriendship.setDstPerson(dest);
        friendshipRepository.save(newFriendship);

    }

    public AccountResponse unBlockUser(Principal principal, int id) throws UnBlockingException, UserUnBlocksHimSelfException, UnBlockingDeletedAccountException {
        Person current = findPerson(principal.getName());
        if (current.getId() == id) throw new UserUnBlocksHimSelfException();
        Person unblocking = findPerson(id);
        if (unblocking.isDeleted()) throw new UnBlockingDeletedAccountException();
        Optional<Friendship> optional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(current.getId(), id);
        if (!isBlockedBy(current.getId(), id, optional)) {
            throw new UnBlockingException();
        }
        Friendship friendship = optional.get();
        if (friendship.getStatus().getCode().equals(FriendshipStatusCode.BLOCKED)
                || friendship.getStatus().getCode().equals(FriendshipStatusCode.WASBLOCKEDBY) && current.getId().equals(friendship.getDstPerson().getId())) {
            friendshipRepository.delete(friendship);
            friendshipStatusRepository.delete(friendship.getStatus());
        } else {
            if (current.getId().equals(friendship.getSrcPerson().getId())) {
                friendship.getStatus().setCode(FriendshipStatusCode.WASBLOCKEDBY);
            } else {
                friendship.getStatus().setCode(FriendshipStatusCode.BLOCKED);
            }
            friendshipStatusRepository.save(friendship.getStatus());
            friendshipRepository.save(friendship);

        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setTimestamp(ZonedDateTime.now().toInstant());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        accountResponse.setData(dateMap);
        return accountResponse;

    }

    /**
     * Src Person BlOCKED Dest Person or
     * Dest person WASBLOCKEDBY Src Person or
     * Src and Dest blocked Each other (DEADLOCK)
     */
    public boolean isBlockedBy(int blocker, int blocked) {
        Optional<Friendship> optional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(blocker, blocked);
        return isBlockedBy(blocker, blocked, optional);
    }

    private boolean isBlockedBy(int blocker, int blocked, Optional<Friendship> optional) {
        return optional.filter(friendship -> (blocker == friendship.getSrcPerson().getId() && friendship.getStatus().getCode().equals(FriendshipStatusCode.BLOCKED))
                || (blocked == friendship.getSrcPerson().getId() && friendship.getStatus().getCode().equals(FriendshipStatusCode.WASBLOCKEDBY))
                || friendship.getStatus().getCode().equals(FriendshipStatusCode.DEADLOCK)).isPresent();
    }

    Page<Person> get10Users(String email, Pageable pageable, List<Integer> blockers) {
        return personRepository.find10Person(email, pageable, blockers);
    }

    List<Integer> getBlockersId(int id) {
        List<Integer> blockers = friendshipRepository.findBlockersIds(id);
        return blockers.isEmpty() ? Collections.singletonList(-1) : blockers;
    }

    private void sendNotification(Friendship friendship) {
        NotificationData notificationData = new NotificationData();
        notificationData.setId(friendship.getId());
        notificationData.setSentTime(Instant.now());
        notificationData.setEventType(NotificationType.FRIEND_REQUEST);
        notificationData.setEntityAuthor(setAuthData(friendship.getSrcPerson()));
        notificationData.setEntityId(notificationData.getEntityAuthor().getId());
        notificationService.sendEvent("friend-notification-response", notificationData, notificationData.getEntityId());

    }

}

