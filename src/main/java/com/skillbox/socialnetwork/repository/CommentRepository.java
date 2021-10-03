package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPostId(int id);
    List<PostComment> findByParentId(Integer id);

    @Query(value = "SELECT * FROM post_comment pc WHERE pc.post_id = :post_id", nativeQuery = true)
    List<PostComment> findPostCommentByPostId(@Param("post_id") Integer id, Pageable pageable);
}
