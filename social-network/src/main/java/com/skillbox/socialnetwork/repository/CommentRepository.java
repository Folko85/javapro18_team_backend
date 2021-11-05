package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    Page<PostComment> findPostCommentsByPostIdAndParentIsNull(int post, Pageable pageable);
}
