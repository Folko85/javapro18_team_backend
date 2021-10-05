package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
//    @Query("SELECT pl " +
//            "FROM PostLike pl " +
//            "WHERE pl.post.id = ?1"
//    )
    List<CommentLike> findPostLikeByItem(int item);

//    @Query("SELECT pl " +
//            "FROM PostLike pl " +
//            "WHERE pl.post.id = ?1 AND pl.person.id = ?2"
//    )
    Optional<CommentLike> findPostLikeByItemAndPerson(int item, int person);
}
