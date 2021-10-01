package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "LEFT JOIN PostLike pl on pl.post.id = p.id " +
            "WHERE p.isBlocked = false AND p.datetime >= ?2 AND p.datetime <= ?3 AND p.postText LIKE  %?1% " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContainingByDate(String query, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "LEFT JOIN PostLike pl on pl.post.id = p.id " +
            "WHERE p.isBlocked = false AND p.postText LIKE  %?1% " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContaining(String query, Pageable pageable);

    Page<Post> findPostById(int id, Pageable pageable);
}
