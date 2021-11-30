package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<PostFile, Integer> {

    PostFile findByUrl(String url);

    @Query("SELECT f FROM PostFile f " +
            "WHERE f.postId = :postId")
    PostFile findByPostId(@Param("postId") Integer postId);

    @Query("SELECT f FROM PostFile f " +
            "WHERE f.commentId = :commentId")
    PostFile findByCommentId(@Param("commentId") Integer commentId);
}
