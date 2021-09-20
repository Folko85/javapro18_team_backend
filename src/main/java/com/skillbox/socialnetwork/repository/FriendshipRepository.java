package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {

}
