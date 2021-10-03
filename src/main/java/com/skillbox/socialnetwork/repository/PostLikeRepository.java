package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    @Query("SELECT pl " +
            "FROM PostLike pl " +
            "WHERE pl.post.id = ?1"
    )
    List<PostLike> findPostLikeByItem(int item);

    @Query("SELECT pl " +
            "FROM PostLike pl " +
            "WHERE pl.post.id = ?1 AND pl.person.id = ?2"
    )
    Optional<PostLike> findPostLikeByItemAndPerson(int item, int person);
}