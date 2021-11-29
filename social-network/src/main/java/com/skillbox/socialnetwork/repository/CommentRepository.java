package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    Page<PostComment> findPostCommentsByPostIdAndParentIsNullOrderByTime(int post, Pageable pageable);

    @Query("SELECT c FROM PostComment c " +
            "WHERE deleted_at < :minusDays")
    List<PostComment> findSoftDeletedCommentsID(@Param("minusDays") LocalDateTime minusDays);
}
