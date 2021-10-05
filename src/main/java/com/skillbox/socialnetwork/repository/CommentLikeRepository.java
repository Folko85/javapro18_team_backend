package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    @Query("SELECT cl " +
            "FROM CommentLike cl " +
            "WHERE cl.postComment.id = ?1"
    )
    List<CommentLike> findCommentLikeByItem(int item);

    @Query("SELECT cl " +
            "FROM CommentLike cl " +
            "WHERE cl.postComment.id = ?1 AND cl.person.id = ?2"
    )
    Optional<CommentLike> findCommentLikeByItemAndPerson(int item, int person);
}
