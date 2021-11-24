package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    Page<PostComment> findPostCommentsByPostIdAndParentIsNullOrderByTime(int post, Pageable pageable);
}
