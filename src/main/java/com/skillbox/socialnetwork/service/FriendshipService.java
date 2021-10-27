package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.ResponseFriendsList;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.StatusFriend;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.exception.BlockAlreadyExistsException;
import com.skillbox.socialnetwork.exception.UnBlockingException;
import com.skillbox.socialnetwork.exception.UserBlocksHimSelfException;
import com.skillbox.socialnetwork.exception.UserUnBlocksHimSelfException;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.FriendshipStatusRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Slf4j
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
        log.debug("метод получения друзей");
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> pageablePersonList = personRepository.findPersonByFriendship(name, person.getId(), FriendshipStatusCode.FRIEND, pageable);
        return getPersonResponse(offset, itemPerPage, pageablePersonList);
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
        FriendsResponse200 response;

        Person srcPerson = personService.findPersonByEmail(principal.getName());
        int srcId = srcPerson.getId();

        Optional<Friendship> optionalFriendship = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcId, id);

        if (optionalFriendship.isPresent()) {
            Friendship friendship = optionalFriendship.get();
            int statusId = friendship.getStatus().getId();

            FriendshipStatus friendshipStatus = friendshipStatusRepository
                    .findById(statusId).orElseThrow(() -> new UsernameNotFoundException("friendship status not found"));
            friendshipStatus.setCode(FriendshipStatusCode.SUBSCRIBED);

            friendship.setStatus(friendshipStatus);

            friendshipStatusRepository.save(friendshipStatus);
            friendshipRepository.save(friendship);

            response = getFriendResponse200("Successfully", "Stop being friends");
        } else {
            response = getFriendResponse200("Unsuccessfully", "Don't stop being friends");
        }
        return response;
    }

    public FriendsResponse200 addNewFriend(int id, Principal principal) {
        log.debug("метод добавления в друзья");

        FriendsResponse200 addFriendResponse = getFriendResponse200("Successfully", "Adding to friends");

        Person srcPerson = personRepository
                .findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("person not found"));
        int srcPersonId = srcPerson.getId();

        Person dstPerson = personService.findPersonById(id).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(srcPersonId, id);

        if (friendshipOptional.isPresent()) {
            FriendshipStatus friendshipStatusById = friendshipStatusRepository
                    .findById(friendshipOptional.get().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("friendship status not found"));
            friendshipStatusById.setTime(LocalDateTime.now());
            friendshipStatusById.setCode(FriendshipStatusCode.FRIEND);

            friendshipStatusRepository.save(friendshipStatusById);
        } else {
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setCode(FriendshipStatusCode.REQUEST);

            FriendshipStatus saveFriendshipStatus = friendshipStatusRepository.save(friendshipStatus);

            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(saveFriendshipStatus);
            newFriendship.setSrcPerson(srcPerson);
            newFriendship.setDstPerson(dstPerson);

            friendshipRepository.save(newFriendship);
        }
        return addFriendResponse;
    }

    public ListResponse getListOfApplications(String name, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> personByStatusCode = friendshipRepository
                .findPersonByStatusCode(name, person.getId(), FriendshipStatusCode.REQUEST, pageable);

        return getPersonResponse(offset, itemPerPage, personByStatusCode);
    }

    public ListResponse recommendedUsers(int offset, int itemPerPage, Principal principal) {
        log.debug("метод получения рекомендованных друзей");
        Person person = findPerson(principal.getName());
        log.debug("поиск рекомендованных друзей для пользователя: ".concat(person.getFirstName()));
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate birthdayPerson = null;
        LocalDate startDate = null;
        LocalDate stopDate = null;

        if (person.getBirthday() != null) {
            birthdayPerson = person.getBirthday();
            startDate = birthdayPerson.minusYears(2);
            stopDate = birthdayPerson.plusYears(2);
        }

        String city = person.getCity();

        Page<Person> personList = null;

        //дата рождения указана, города не указан
        if (birthdayPerson != null && city == null) {
            log.debug("дата рождения указана, города не указан");
            //подбираем пользователей, возрост которых отличается на +-2 года
            personList = personRepository
                    .findPersonByBirthday(person.getEMail(), startDate, stopDate, pageable);

            //дата рождения указана и город указан
        } else if (birthdayPerson != null) {
            log.debug("дата рождения указана и город указан");
            //подбираем пользователей, возрост которых отличается на +-2 года и в городе проживания
            personList = personRepository
                    .findPersonByBirthdayAndCity(person.getEMail(), startDate, stopDate, city, pageable);

            //дата рождения не указана, город указан
        } else if (city != null) {
            log.debug("дата рождения не указана, город указан");
            personList = personRepository.findPersonByCity(city, pageable);

        } else {
            log.debug("ни дата рождения, ни город не указан. выбираем рандомных 10 пользователей");
            //выбираем 10 рандомных пользователей
            personList = get10Users(pageable);
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
    private ListResponse getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        ListResponse postResponse = new ListResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(getPerson4Response(pageablePersonList.toList()));
        return postResponse;
    }

    private ListResponse getPersonResponseList(int offset, int itemPerPage, List<Person> personList) {
        ListResponse postResponse = new ListResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal(personList.size());
        postResponse.setData(getPerson4Response(personList));
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
    public AccountResponse blockUser(Principal principal, int id) throws BlockAlreadyExistsException, UserBlocksHimSelfException {
        Person current = findPerson(principal.getName());
        if (current.getId() == id) throw new UserBlocksHimSelfException();
        Person blocking = findPerson(id);
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

    public AccountResponse unBlockUser(Principal principal, int id) throws UnBlockingException, UserUnBlocksHimSelfException {
        Person current = findPerson(principal.getName());
        if (current.getId() == id) throw new UserUnBlocksHimSelfException();
        Person unblocking = findPerson(id);
        Optional<Friendship> optional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(current.getId(), id);
        if (!isBlockedBy(current.getId(), id, optional)) {
            throw new UnBlockingException();
        }
        Friendship friendship = optional.get();
        if (friendship.getStatus().getCode().equals(FriendshipStatusCode.BLOCKED)) {
            friendshipRepository.delete(friendship);
            friendshipStatusRepository.delete(friendship.getStatus());
        } else {
            if (current.getId().equals(friendship.getSrcPerson().getId())) {
                friendship.getStatus().setCode(FriendshipStatusCode.WASBLOCKEDBY);
                friendshipStatusRepository.save(friendship.getStatus());
            } else {
                friendship.getStatus().setCode(FriendshipStatusCode.BLOCKED);
                friendshipStatusRepository.save(friendship.getStatus());
            }
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
        if (optional.isPresent()) {
            return (blocker == optional.get().getSrcPerson().getId() && optional.get().getStatus().getCode().equals(FriendshipStatusCode.BLOCKED))
                    || (blocked == optional.get().getSrcPerson().getId() && optional.get().getStatus().getCode().equals(FriendshipStatusCode.WASBLOCKEDBY))
                    || optional.get().getStatus().getCode().equals(FriendshipStatusCode.DEADLOCK);
        }
        return false;
    }

    Page<Person> get10Users(Pageable pageable) {
        return personRepository.find10Person(pageable);
    }

}
