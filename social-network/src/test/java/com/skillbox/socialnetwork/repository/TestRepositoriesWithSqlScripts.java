package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.AbstractTestsWithSqlScripts;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.service.FriendshipService;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
@Slf4j
public class TestRepositoriesWithSqlScripts extends AbstractTestsWithSqlScripts {

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FriendshipService friendshipService;

    //Пользователи с этими почтами заблокировали с почтой blockedEmail
    private static final String[] blockersEmails = {
            "jboner0@domainmarket.com",
            "cfurby1@webeden.co.uk",
            "bjacobowits2@wsj.com",
            "jpeagram3@virginia.edu",
            "gdomerc5@unesco.org",
    };
    //Пользователи с этими почтами имеют связь френдшип, но с неблокирующими статусами
    private static final String[] whitePersons = {
            "kmattussevich6@alibaba.com",
            "ipinar7@wisc.edu",
            "goshea8@apple.com",
            "sdeavin9@booking.com",
            "xbanaszewskia@delicious.com",
            "cbuncherb@reverbnation.com"
    };

    //Эти пользователи имеют либо дружат с Target, либо Target подписан на них или отправил заявку
    private static final String[] banlistForRecomendations = {
            "kmattussevich6@alibaba.com",
            "ipinar7@wisc.edu",
            "sdeavin9@booking.com",
            "cbuncherb@reverbnation.com"
    };

    private static final List<Integer> blockersIds = new ArrayList<>();
    private static final List<Integer> whitePersonsIds = new ArrayList<>();
    private static final List<Integer> banlistForRecomendationsIds = new ArrayList<>();
    private static Person target;

    @BeforeEach
    public void setUpData(@Autowired PersonRepository personRepository) {

        for (String blockersEmail : blockersEmails) {
            blockersIds.add(personRepository.findByEMail(blockersEmail).get().getId());
        }

        for (String x : whitePersons) {
            whitePersonsIds.add(personRepository.findByEMail(x).get().getId());
        }

        for (String banlistForRecomendations : banlistForRecomendations) {
            banlistForRecomendationsIds.add(personRepository.findByEMail(banlistForRecomendations).get().getId());
        }

        //Target Person
        String blockedEmail = "kburgan4@comcast.net";
        target = personRepository.findByEMail(blockedEmail).get();
    }

    @Test
    @DisplayName("Проверяем список id пользователей, которые заблокировали пользователя с почтой blockedEmail")
    public void testFindBlockersIds() {
        log.info("Target id: {}, Email: {}", target.getId(), target.getEMail());
        List<Integer> checkingIds = personRepository.findBlockersIds(target.getId());
        Assertions.assertArrayEquals(blockersIds.toArray(), checkingIds.toArray(), "Массивы с id не равны");
        HashSet<Integer> ids = new HashSet<>(checkingIds);
        whitePersonsIds.forEach(x -> {
            if (ids.contains(x)) {
                Optional<Friendship> friendsh = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(target.getId(), x);
                Assertions.assertFalse(friendsh.isEmpty(), "Получили Пустой Френдиш");
                Assertions.fail("В списке блокирующих был найден недопустимый id ");
                Friendship friendship = friendsh.get();
                log.error("FriendShip: Src id and email : {} , {} # status: {}  # Dst id and email : {} , {} ",
                        friendship.getSrcPerson().getId(), friendship.getSrcPerson().getEMail(), friendship.getStatus(),
                        friendship.getDstPerson().getId(), friendship.getDstPerson().getEMail()
                );
            }
        });
        //Этого тестового пользователя никто не блокировал
        Person person = personRepository.findByEMail("jboner0@domainmarket.com").get();
        Assertions.assertTrue(personRepository.findBlockersIds(person.getId()).isEmpty());

    }


    String text = "";
    long dateF = 0;
    long dateT = -1;
    int offset = 0;
    int itemPerPage = 20;

    @Test
    @DisplayName("Проверяем получение постов")
    public void getPosts() {
        log.info("Проверяем получение постов для пользователя без блокировок");

        Person person = personRepository.findByEMail("jboner0@domainmarket.com").get();
        List<Integer> personList = personRepository.findBlockersIds(person.getId());
        personList.add(-1);
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Instant dateFrom = Instant.ofEpochMilli(dateF);
        Instant dateTo = Instant.now();
        Page<Post> postList = postRepository.findPostsByTextContainingByDateExcludingBlockersWithoutTags("", "", dateFrom, dateTo, pageable, personList);
        Assertions.assertFalse(postList.isEmpty(), "Получили пустой список постов");
        Assertions.assertEquals(postList.get().count(), itemPerPage, "Получили постов меньше, чем дефолтное значение 20");
        Page<Post> postListR = postRepository.findPostsByTextContainingByDateExcludingBlockersWithoutTags("", "R", dateFrom, dateTo, pageable, personList);
        Assertions.assertTrue(postListR.isEmpty(), "Получили не пустой список постов с автором R");
        Page<Post> postListsG = postRepository.findPostsByTextContainingByDateExcludingBlockersWithoutTags("", "G", dateFrom, dateTo, pageable, personList);
        Assertions.assertEquals(4L, postListsG.get().count(), "Количество постов автора G не равно 4");


        log.info("Проверяем получение постов для пользователя Target");
        HashSet<Integer> blockersHashSet = new HashSet<>(blockersIds);

        Pageable pageable2 = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> postList2 = postRepository.findPostsByTextContainingByDateExcludingBlockersWithoutTags("", "", dateFrom, dateTo, pageable, blockersIds);
        Assertions.assertFalse(postList2.isEmpty(), "Получили пустой список постов для Target");
        AtomicInteger countOfTargetPosts = new AtomicInteger();
        postList2.toList().forEach(post -> {
            Assertions.assertFalse(blockersHashSet.contains(post.getPerson().getId()), "Список постов содержит пост блокера Target");
            if (post.getPerson().getId().equals(target.getId())) {
                countOfTargetPosts.getAndIncrement();
            }
        });
        Assertions.assertEquals(2, countOfTargetPosts.get(), "Не получили два поста Target");
    }

    @Test
    @DisplayName("Проверяем получение списка заблокировавших пользователя, пользователей, которым отправлена заявка или на которых подписан Target")
    public void findPersonSubscribesAndOrRequestsTest() {
        log.info("Target id: {}, Email: {}", target.getId(), target.getEMail());
        List<Integer> personsIds = personRepository.findPersonRelastionShips(target.getId());
        Assertions.assertFalse(personsIds.contains(target.getId()), "Ответ тестируемого метода содержит id Target");
        List<Integer> checkRelationShip = new ArrayList<>();
        checkRelationShip.addAll(blockersIds);
        checkRelationShip.addAll(whitePersonsIds);
        Collections.sort(checkRelationShip);
        Assertions.assertArrayEquals(checkRelationShip.toArray(), personsIds.toArray(), "Ответ тестируемого метода не равен ожидаемому");

    }

    private final String[] personsForDrCheck = {
            // 1977-03-22
            "esaywardn@1688.com",
            // 1980-04-01
            "bkirkhamh@wiley.com"
    };

    @AfterEach
    public void cleanup() {
        friendshipRepository.deleteAll();
        postRepository.deleteAll();
        personRepository.deleteAll();
        blockersIds.clear();
    }


    @Test
    @DisplayName("Проверяем рекомендации для Москвича без блокировок")
    public void recommendedUsersTest() {
        log.debug("метод получения рекомендованных друзей");
        //Target Person -Москвич 1979-03-13
        String targetMoscwich = "jboner0@domainmarket.com";
        Person personTargetMoscwich = personRepository.findByEMail(targetMoscwich).get();
        log.debug("поиск рекомендованных друзей для пользователя: ".concat(personTargetMoscwich.getFirstName()));
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        LocalDate startDate = null;
        LocalDate stopDate = null;

        //Не хотим в рекомендациях блокирующих
        List<Integer> blockers = personRepository.findBlockersIds(personTargetMoscwich.getId());
        Assertions.assertTrue(blockers.isEmpty());

        blockers.add(personTargetMoscwich.getId());
        Page<Person> vseMoscvichiBezTarget = personRepository.findByOptionalParametrs(
                "", "", startDate, stopDate, personTargetMoscwich.getCity(), "", pageable, blockers);
        //Тестовые пользователи с тем же городом - Moscow
        String moscwichAmantri = "amantrippd@addtoany.com";
        String moscwichCropton = "bcroptono@cdbaby.com";
        //а Этот Москвич и кинул Target в подписчики
        String moscwichUdalilMoscwicha = "lhannaf@msn.com";
        List<Integer> idsVsehMoscvicheyBezTarget = Arrays.asList(
                personRepository.findByEMail(moscwichAmantri).get().getId(),
                personRepository.findByEMail(moscwichCropton).get().getId(),
                personRepository.findByEMail(moscwichUdalilMoscwicha).get().getId()
        );
        Collections.sort(idsVsehMoscvicheyBezTarget);
        Assertions.assertEquals(3, vseMoscvichiBezTarget.getTotalElements());

        List<Integer> foundMoscwichi = new ArrayList<>();
        vseMoscvichiBezTarget.toList().forEach(p -> foundMoscwichi.add(p.getId()));
        Collections.sort(foundMoscwichi);

        Assertions.assertArrayEquals(idsVsehMoscvicheyBezTarget.toArray(), foundMoscwichi.toArray());

        //Не хотим в рекомендациях друзей, кому отправили запросы в друзья и на кого подписан
        List<Integer> personSubscribesAndOrRequestsIds = personRepository.findPersonRelastionShips(personTargetMoscwich.getId());
        //Друг Москвича
        String drugMoscwichaFurby = "cfurby1@webeden.co.uk";
        //Moscwich отправил этому заявку и он того же года рождения 1979-07-19
        String requestMoscwicha = "sdeavin9@booking.com";
        //Этот сделал москвича своим подписчиком
        String subcribeMoscwicha = "cbuncherb@reverbnation.com";
        List<Integer> neHotimIch = Arrays.asList(
                personRepository.findByEMail(drugMoscwichaFurby).get().getId(),
                personRepository.findByEMail(requestMoscwicha).get().getId(),
                personRepository.findByEMail(subcribeMoscwicha).get().getId(),
                personRepository.findByEMail(moscwichUdalilMoscwicha).get().getId(),
                personRepository.findByEMail("kmattussevich6@alibaba.com").get().getId(),
                target.getId()
        );
        Assertions.assertEquals(neHotimIch.size(), personSubscribesAndOrRequestsIds.size());
        Collections.sort(neHotimIch);
        Collections.sort(personSubscribesAndOrRequestsIds);
        Assertions.assertArrayEquals(neHotimIch.toArray(), personSubscribesAndOrRequestsIds.toArray());
        blockers.addAll(personSubscribesAndOrRequestsIds);
        //Пусть ДР не указан
        Page<Person> personsBezDr = personRepository.findByOptionalParametrs(
                "", "", startDate, stopDate, personTargetMoscwich.getCity(), "", pageable, blockers);
        Assertions.assertEquals(2, personsBezDr.getTotalElements());
        List<Integer> idsVsehMoscvicheyBezTargetIBezUdal = Arrays.asList(
                personRepository.findByEMail(moscwichAmantri).get().getId(),
                personRepository.findByEMail(moscwichCropton).get().getId()
        );
        Collections.sort(idsVsehMoscvicheyBezTargetIBezUdal);
        List<Integer> foundCitizens = new ArrayList<>();
        personsBezDr.toList().forEach(p -> foundCitizens.add(p.getId()));
        Collections.sort(foundCitizens);
        Assertions.assertArrayEquals(idsVsehMoscvicheyBezTargetIBezUdal.toArray(), foundCitizens.toArray());
        //Пусть город не указан
        if (personTargetMoscwich.getBirthday() != null) {
            //подбираем пользователей, возрост которых отличается на +-2 года
            LocalDate birthdayPerson = personTargetMoscwich.getBirthday();
            // 1979-03-13
            startDate = birthdayPerson.minusYears(2);
            stopDate = birthdayPerson.plusYears(2);
        }

        List<Integer> personsForDrCheckList = new ArrayList<>();
        for (String p : personsForDrCheck) {
            personsForDrCheckList.add(personRepository.findByEMail(p).get().getId());
        }
        Collections.sort(personsForDrCheckList);

        Page<Person> personsBezCity = personRepository.findByOptionalParametrs(
                "", "", startDate, stopDate, "", "", pageable, blockers);
        Assertions.assertEquals(2, personsBezCity.getTotalElements());

        List<Integer> personsBezCityList = new ArrayList<>();
        personsBezCity.toList().forEach(p -> personsBezCityList.add(p.getId()));
        Collections.sort(personsBezCityList);
        Assertions.assertArrayEquals(personsForDrCheckList.toArray(), personsBezCityList.toArray());

        // Получаем для подбора по городу
        String city = Strings.hasText(personTargetMoscwich.getCity()) ? personTargetMoscwich.getCity() : "";
        Page<Person> personFirstPage = personRepository.findByOptionalParametrs(
                "", "", startDate, stopDate, city, "", pageable, blockers);
        //Нет таких пользователь +-2 года и из одного города
        Assertions.assertTrue(personFirstPage.isEmpty());

        if ((int) personFirstPage.getTotalElements() < 10) {
            Pageable additionalPageable = PageRequest.of(0, (int) (10 - personFirstPage.getTotalElements()));
            personFirstPage.get().forEach(p -> blockers.add(p.getId()));
            Page<Person> additionalPersonPage = friendshipService.get10Users(personTargetMoscwich.getEMail(), additionalPageable, blockers);
            List<Person> additionalPersonList = additionalPersonPage.stream().toList();
            List<Person> personFirstList = personFirstPage.stream().collect(Collectors.toList());
            personFirstList.addAll(additionalPersonList);
            personFirstPage = new PageImpl<>(personFirstList, pageable, personFirstList.size());
        }

        Assertions.assertEquals(10, personFirstPage.getTotalElements());

        if ((int) personsBezDr.getTotalElements() < 10) {
            Pageable additionalPageable = PageRequest.of(0, (int) (10 - personsBezDr.getTotalElements()));
            personsBezDr.get().forEach(p -> blockers.add(p.getId()));
            Page<Person> additionalPersonPage = friendshipService.get10Users(personTargetMoscwich.getEMail(), additionalPageable, blockers);
            List<Person> additionalPersonList = additionalPersonPage.stream().toList();
            List<Person> personFirstList = personsBezDr.stream().collect(Collectors.toList());
            personFirstList.addAll(additionalPersonList);

            HashSet<Integer> checkDuplicates = new HashSet<>();
            personFirstList.forEach(p -> checkDuplicates.add(p.getId()));
            Assertions.assertEquals(checkDuplicates.size(), personFirstList.size());

            personsBezDr = new PageImpl<>(personFirstList, pageable, personFirstList.size());
        }
        Assertions.assertEquals(10, personsBezDr.getTotalElements());

        if ((int) personsBezCity.getTotalElements() < 10) {
            Pageable additionalPageable = PageRequest.of(0, (int) (10 - personsBezCity.getTotalElements()));
            personsBezCity.get().forEach(p -> blockers.add(p.getId()));
            Page<Person> additionalPersonPage = friendshipService.get10Users(personTargetMoscwich.getEMail(), additionalPageable, blockers);
            List<Person> additionalPersonList = additionalPersonPage.stream().toList();
            List<Person> personFirstList = personsBezCity.stream().collect(Collectors.toList());
            personFirstList.addAll(additionalPersonList);

            HashSet<Integer> checkDuplicates = new HashSet<>();
            personFirstList.forEach(p -> checkDuplicates.add(p.getId()));
            Assertions.assertEquals(checkDuplicates.size(), personFirstList.size());

            personsBezCity = new PageImpl<>(personFirstList, pageable, personFirstList.size());
        }
        Assertions.assertEquals(10, personsBezCity.getTotalElements());
    }

    @Test
    public void testMethods() {
        List<Integer> friendsIds = personRepository.findFriendsIds(target.getId());
        List<Integer> checkFriendsIds = Arrays.asList(
                personRepository.findByEMail("kmattussevich6@alibaba.com").get().getId(),
                personRepository.findByEMail("ipinar7@wisc.edu").get().getId()
        );
        Collections.sort(checkFriendsIds);
        Assertions.assertArrayEquals(checkFriendsIds.toArray(), friendsIds.toArray());
        List<Integer> friendsOfFriendsIds = personRepository.findFriendsOfFriendsIds(target.getId());
        List<Integer> checkFriendsOfFriendsIds = Arrays.asList(
                personRepository.findByEMail("kcranmerc@va.gov").get().getId(),
                personRepository.findByEMail("jboner0@domainmarket.com").get().getId(),
                personRepository.findByEMail("cwolstencroftg@cnn.com").get().getId(),
                personRepository.findByEMail("amantrippd@addtoany.com").get().getId(),
                personRepository.findByEMail("awabee@e-recht24.de").get().getId()
        );
        Collections.sort(checkFriendsOfFriendsIds);
        Assertions.assertArrayEquals(checkFriendsOfFriendsIds.toArray(), friendsOfFriendsIds.toArray());
        List<Integer> friendsAndFriendsOfFriendsAndSubscribesIds = personRepository.findFriendsAndFriendsOfFriendsAndSubscribesIds(target.getId());
        List<Integer> checkFriendsAndFriendsOfFriendsAndSubscribesIds = new ArrayList<>();
        checkFriendsAndFriendsOfFriendsAndSubscribesIds.addAll(checkFriendsIds);
        checkFriendsAndFriendsOfFriendsAndSubscribesIds.addAll(checkFriendsOfFriendsIds);
        //Subcriber
        checkFriendsAndFriendsOfFriendsAndSubscribesIds.add(personRepository.findByEMail("cbuncherb@reverbnation.com").get().getId());
        //Request
        checkFriendsAndFriendsOfFriendsAndSubscribesIds.add(personRepository.findByEMail("sdeavin9@booking.com").get().getId());
        Collections.sort(checkFriendsAndFriendsOfFriendsAndSubscribesIds);
        Assertions.assertArrayEquals(checkFriendsAndFriendsOfFriendsAndSubscribesIds.toArray(), friendsAndFriendsOfFriendsAndSubscribesIds.toArray());
        List<Integer> friendsAndFriendsOfFriendsAndSubscribesFiltered = friendshipService.getFriendsAndFriendsOfFriendsAndSubscribesFiltered(target.getId());
        Assertions.assertFalse(friendsAndFriendsOfFriendsAndSubscribesFiltered.contains(personRepository.findByEMail("jboner0@domainmarket.com").get().getId()));
    }
}
