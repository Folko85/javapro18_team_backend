package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.exception.*;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.FriendshipStatusRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = {NetworkApplication.class})
public class FriendShipServiceTest extends AbstractTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipStatusRepository friendshipStatusRepository;

    @Autowired
    FriendshipService friendshipService;

    Person oleg;
    Person petr;
    Principal principalOleg;
    Principal principalPetr;

    @BeforeEach
    public void setup() {
        super.setup();
        oleg = new Person();
        oleg.setFirstName("Олег");
        oleg.setLastName("Иванов");
        oleg.setDateAndTimeOfRegistration(LocalDateTime.now());
        oleg.setEMail("ivanov@test.ru");
        oleg.setPassword("password");
        oleg.setApproved(true);
        oleg.setLastOnlineTime(LocalDateTime.now());
        oleg.setBlocked(false);

        petr = new Person();
        petr.setFirstName("Петр");
        petr.setLastName("Петров");
        petr.setDateAndTimeOfRegistration(LocalDateTime.now());
        petr.setEMail("petrov@test.ru");
        petr.setPassword("password");
        petr.setApproved(true);
        petr.setLastOnlineTime(LocalDateTime.now());
        petr.setBlocked(false);

        oleg = personRepository.save(oleg);
        petr = personRepository.save(petr);
        principalOleg = () -> oleg.getEMail();
        principalPetr = () -> petr.getEMail();
    }

    @AfterEach
    public void resetDb() {
        friendshipRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @DisplayName("Проверяем Создание Блокировки без связи между пользователями")
    public void createBlock() throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException {
        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());
        friendshipService.blockUser(principalOleg, petr.getId());
        Optional<Friendship> blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        Friendship block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.BLOCKED,block.getStatus().getCode());
    }

    @Test
    @DisplayName("Проверяем Создание Блокировки в ответ")
    public void createBlockWithBlock() throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException {
        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());

        //Проверяем, что можем заблокировать в ответ
        friendshipService.blockUser(principalOleg, petr.getId());
        friendshipService.blockUser(principalPetr, oleg.getId());
        Optional<Friendship> blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        Friendship block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.DEADLOCK,block.getStatus().getCode());

        createAndDropIfExists(oleg, petr, FriendshipStatusCode.WASBLOCKEDBY);
        friendshipService.blockUser(principalOleg, petr.getId());
        blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.DEADLOCK,block.getStatus().getCode());

    }

    @Test
    @DisplayName("Проверяем Создание Блокировки со связью между пользователями")
    public void createBlockWithReleation() throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException {
        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());

        //Создаём дружбу
        Friendship friendship = createAndDropIfExists(oleg, petr, FriendshipStatusCode.FRIEND);

        //Проверяем, что src может заблокировать друга
        friendshipService.blockUser(principalOleg, petr.getId());
        Optional<Friendship> blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        Friendship block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.BLOCKED,block.getStatus().getCode());

        //Проверяем, что dst может заблокировать друга
        createAndDropIfExists(oleg, petr, FriendshipStatusCode.FRIEND);
        friendshipService.blockUser(principalPetr, oleg.getId());
        blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.WASBLOCKEDBY,block.getStatus().getCode());

    }

    @Test
    @DisplayName("Проверяем Разблокировку")
    public void testUnblock() throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException, UnBlockingException, UnBlockingDeletedAccountException, UserUnBlocksHimSelfException {
        Optional<Friendship> friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());

        friendshipService.blockUser(principalOleg, petr.getId());
        friendshipService.unBlockUser(principalOleg, petr.getId());
        friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());

        createAndDropIfExists(oleg, petr, FriendshipStatusCode.WASBLOCKEDBY);
        friendshipService.unBlockUser(principalPetr, oleg.getId());
        friendshipOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertTrue(friendshipOptional.isEmpty());

        createAndDropIfExists(oleg, petr, FriendshipStatusCode.DEADLOCK);
        friendshipService.unBlockUser(principalOleg, petr.getId());
        Optional<Friendship> blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        Friendship block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.WASBLOCKEDBY,block.getStatus().getCode());

        createAndDropIfExists(oleg, petr, FriendshipStatusCode.DEADLOCK);
        friendshipService.unBlockUser(principalPetr, oleg.getId());
        blockOptional = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        Assertions.assertFalse(blockOptional.isEmpty());
        block = blockOptional.get();
        Assertions.assertEquals(oleg.getId(), block.getSrcPerson().getId());
        Assertions.assertEquals(petr.getId(), block.getDstPerson().getId());
        Assertions.assertEquals(FriendshipStatusCode.BLOCKED,block.getStatus().getCode());

    }

    public Friendship createAndDropIfExists(Person src, Person dest, FriendshipStatusCode friendshipStatusCode ){
        Optional<Friendship> check = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(oleg.getId(), petr.getId());
        if(check.isPresent()){
            FriendshipStatus friendshipStatus =check.get().getStatus();
            friendshipRepository.delete(check.get());
            friendshipStatusRepository.delete(friendshipStatus);
        }
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setCode(friendshipStatusCode);
        FriendshipStatus saveFriendshipStatus = friendshipStatusRepository.save(friendshipStatus);
        Friendship newFriendship = new Friendship();
        newFriendship.setStatus(saveFriendshipStatus);
        newFriendship.setSrcPerson(src);
        newFriendship.setDstPerson(dest);
        return friendshipRepository.save(newFriendship);
    }



}
