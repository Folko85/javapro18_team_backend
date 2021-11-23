package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.entity.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipForTestRepository extends PagingAndSortingRepository<Friendship, Integer>{

    @Query("SELECT f FROM Friendship f " +
            "WHERE f.srcPerson.id = ?1 AND f.status.code = 'WASBLOCKEDBY' " +
            "OR f.dstPerson.id = ?1 AND f.status.code = 'BLOCKED' " +
            "OR ( f.dstPerson.id = ?1 OR f.srcPerson.id = ?1 ) AND f.status.code = 'DEADLOCK' ")
    List<Friendship> findBlocks(int src);

}
