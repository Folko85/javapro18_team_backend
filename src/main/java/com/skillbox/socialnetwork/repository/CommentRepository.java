package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByParentId(Integer id);
}
