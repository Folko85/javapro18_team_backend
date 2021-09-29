package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
}
