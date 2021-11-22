package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    Page<PostComment> findPostCommentsByPostIdAndParentIsNull(int post, Pageable pageable);

    @Query("DELETE " +
            "FROM PostComment " +
            "WHERE isDeleted = true " +
            "AND delete_at <= :commentDelete")
    void deleteAfterSoft(@Param("commentDelete") LocalDateTime commentDelete);
}
