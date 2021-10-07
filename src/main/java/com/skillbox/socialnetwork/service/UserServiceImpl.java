package com.skillbox.socialnetwork.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.api.request.UserRequestModel;
import com.skillbox.socialnetwork.api.request.UserUpdateWithInstantRequestModel;
import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.time.*;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.ZoneOffset.UTC;

@Service
public class UserServiceImpl {

   private AccountRepository accountRepository;
   private PostService postService;
   private PostRepository postRepository;
   private Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
   public UserServiceImpl(AccountRepository accountRepository, PostService postService, PostRepository postRepository){
       this.accountRepository=accountRepository;
       this.postService=postService;
       this.postRepository=postRepository;
   }

    public UserRest getUserByEmail(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        UserRest userRest = new UserRest();
        convertUserToUserRest(person, userRest);
        return userRest;
    }
    public  UserRest getUserById(Integer id){
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(""+id));
        UserRest userRest = new UserRest();
        convertUserToUserRest(person, userRest);
        return userRest;

    }
    public  UserRest updateUser(UserRest updates){
       Person person = accountRepository.findByEMail(updates.getEMail())
                .orElseThrow(() -> new UsernameNotFoundException(""+updates.getEMail()));
       String updatedName =updates.getFirstName().isEmpty() ? person.getFirstName() : updates.getFirstName();
       person.setFirstName(updatedName);
       String updatedLastName=updates.getLastName().isEmpty() ? person.getLastName() : updates.getLastName();
       person.setLastName(updatedLastName);
       person.setPhone(updates.getPhone());
       person.setAbout(updates.getAbout());
       person.setBirthday(covertToLocalDate(updates.getBirthday()));
       person.setMessagesPermission(updates.getMessagesPermission()==null ? person.getMessagesPermission() : updates.getMessagesPermission());
       Person updatedPerson = accountRepository.save(person);
       UserRest updated= new UserRest();
       convertUserToUserRest(updatedPerson, updated);
       return updated;
    }

    public List<PostWallData> getUserWall(int id, Integer offset, Integer itemPerPage){
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(""+id));
        UserRest userRest = new UserRest();
        convertUserToUserRest(person, userRest);
        List<PostWallData> postWallData = postService.getPastWallData(offset, itemPerPage, userRest);
        return postWallData;

    }

    public void deleteUser(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        accountRepository.delete(person);
    }

    /**
     * {@link TODO}
     *
     *
     *
     *
     */
    public UserRequestModel updateUser(HttpEntity<String> httpEntity){
        ObjectMapper objectMapper = new ObjectMapper(); objectMapper.registerModule(new JavaTimeModule());
        UserRequestModel userRequestModel =new UserRequestModel();
        System.out.println(httpEntity.getBody());
        try {
            ObjectNode node = new ObjectMapper().readValue(httpEntity.getBody(), ObjectNode.class);
            if(node.get("birth_date")==null){
                node =node.put("birth_date", "null");
                System.out.println("#################"+node.get("birth_day"));
                userRequestModel = objectMapper.readValue(node.toString(), UserRequestModel.class);
            }else{
                Matcher matcher = pattern.matcher(node.get("birth_date").asText());
                if(matcher.find()){
                    node = node.put("birth_date", convertLocalDate(LocalDate.parse(matcher.group())));
                    userRequestModel = objectMapper.readValue(node.toString(), UserRequestModel.class);
                }
            }
        }
        catch (JsonProcessingException e){
            System.out.println("EXCEEEPTION");
        }
        return userRequestModel;
    }


    public static long convertLocalDate(LocalDate localDate){
       if(localDate==null) return 0;
       java.sql.Date date = java.sql.Date.valueOf(localDate);
       return date.getTime() / 1000;
    }
    public static LocalDate covertToLocalDate(long day) {
        if(day==0) return null;
        java.sql.Date date = new Date(day*1000);
        return date.toLocalDate();
    }
    public  static  long convertLocalDateTime(LocalDateTime localDateTime){
       if(localDateTime==null )
           return 0;
       return localDateTime.toEpochSecond(UTC);

   }
   public  static LocalDateTime convertToLocalDateTime(long date){
       if(date==0) return null;
       return  LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);
   }

    public static  void conventionsFromPersonTimesToUserRest(Person person, UserRest userRest){
      userRest.setLastOnlineTime(convertLocalDateTime(person.getLastOnlineTime()));
      userRest.setDateAndTimeOfRegistration(convertLocalDateTime(person.getDateAndTimeOfRegistration()));
      userRest.setBirthday(convertLocalDate(person.getBirthday()));

   }
   public static void convertUserToUserRest(Person person, UserRest userRest ){
       BeanUtils.copyProperties(person,userRest );
       userRest.setCountry(null);
       userRest.setCity(null);
       conventionsFromPersonTimesToUserRest(person, userRest);
   }

    public PostWallData createPost(int id, long publishDate, PostRequest postRequest, Principal principal) {
        UserRest userRest = getUserById(id);
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        if (userRest.getId()!=person.getId())
            throw new IllegalArgumentException();
        return postService.createPost(publishDate, postRequest, userRest);
    }
}
