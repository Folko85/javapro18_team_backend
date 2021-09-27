package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.PostDTO.PostWallData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static com.skillbox.socialnetwork.service.CommentService.getCommentData4Response;
import static com.skillbox.socialnetwork.service.CommentService.getCommentWallData4Response;
import static java.time.ZoneOffset.UTC;

@Service
public class UserServiceImpl {

   private AccountRepository accountRepository;
   private PostService postService;
   private PostRepository postRepository;

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
        Place city= new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;
    }
    public  UserRest getUserById(Integer id){
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(""+id));
        UserRest userRest = new UserRest();
        convertUserToUserRest(person, userRest);
        Place city= new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;

    }
    public  UserRest updateUser(UserRest updates){
       Person person = accountRepository.findByEMail(updates.getEMail())
                .orElseThrow(() -> new UsernameNotFoundException(""+updates.getEMail()));
       person.setFirstName(updates.getFirstName());
       person.setLastName(updates.getLastName());
       person.setPhone(updates.getPhone());
       person.setAbout(updates.getAbout());
       person.setBirthday(covertToLocalDate(updates.getBirthday()));
       person.setMessagesPermission(updates.getMessagesPermission());
       Person updatedPerson = accountRepository.save(person);
       UserRest updated= new UserRest();
       BeanUtils.copyProperties(updatedPerson, updated);
       updated.setBirthday(convertLocalDate(updatedPerson.getBirthday()));
       updated.setDateAndTimeOfRegistration(convertLocalDateTime(updatedPerson.getDateAndTimeOfRegistration()));
       return updated;
    }
    public List<PostWallData> getUserWall(int id, Integer offset, Integer itemPerPage){
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(""+id));
        Pageable pageable = PageRequest.of(offset/itemPerPage, itemPerPage);
        Page<Post> page =postRepository.findUserPost(id, pageable);
        List<Post> posts =page.getContent();
        List<PostWallData> postWallData = getPastWallData(posts);

        return postWallData;

    }


    public void deleteUser(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        accountRepository.delete(person);
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

   public static  void conventionsFromPersonTimesToUserRest(Person person, UserRest userRest){
      userRest.setLastOnlineTime(convertLocalDateTime(person.getLastOnlineTime()));
      userRest.setDateAndTimeOfRegistration(convertLocalDateTime(person.getDateAndTimeOfRegistration()));
      userRest.setBirthday(convertLocalDate(person.getBirthday()));

   }
   public static void convertUserToUserRest(Person person, UserRest userRest ){
       BeanUtils.copyProperties(person,userRest );
       conventionsFromPersonTimesToUserRest(person, userRest);
   }

   private List<PostWallData> getPastWallData(List<Post> posts){
       List<PostWallData> postWallDataList = new ArrayList<>();
       posts.forEach(post -> {
           PostWallData postWallData = getPostWallData(post);
           postWallDataList.add(postWallData);
       });
       return postWallDataList;
   }

    private PostWallData getPostWallData(Post post) {
        PostWallData postWallData = new PostWallData();
        postWallData.setPostText(post.getPostText());
        UserRest userRest = new UserRest();
        convertUserToUserRest(post.getPerson(), userRest);
        postWallData.setAuthor(userRest);
        postWallData.setComments(getCommentWallData4Response(post.getComments()));
        postWallData.setId(post.getId());
        postWallData.setLikes(post.getPostLikes().size());
        postWallData.setTime(convertLocalDateTime(post.getDatetime()));
        postWallData.setTitle(post.getTitle());
        postWallData.setBlocked(post.isBlocked());
        postWallData.setType("POSTED");
        return postWallData;
    }


}
