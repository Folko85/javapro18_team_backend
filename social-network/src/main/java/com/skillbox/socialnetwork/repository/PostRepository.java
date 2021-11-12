package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "WHERE p.isBlocked = false AND p.datetime >= ?2 AND p.datetime <= ?3 AND p.datetime <= CURRENT_TIMESTAMP AND p.postText LIKE  %?1% AND per.firstName LIKE  %?4% " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContainingByDate(String query, Instant dateFrom, Instant dateTo,String author, Pageable pageable);
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "WHERE p.isBlocked = false AND per.isDeleted = false AND p.postText LIKE  %?1% AND p.datetime <= CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContaining(String query, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id = ?1 and p.isBlocked = false")
    Optional<Post> findPostById(int id);
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "WHERE p.isBlocked = false AND p.person.id = ?1 " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByPersonId(int id, Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "WHERE p.isBlocked = false AND per.id=?1 "+
            "ORDER BY p.datetime DESC")
    Page<Post> findUserPost(int id, Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "WHERE p.isBlocked = false AND per.isDeleted = false AND p.person.id = ?1 AND p.datetime <= CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByPersonIdAndCurrentDate(int id, Pageable pageable);


    @Query( nativeQuery = true, value =
            "SELECT * " +
            "FROM post p " +
            "JOIN post2tag pt ON pt.post_id = p.id " +
            "JOIN tag t ON t.id = pt.tag_id " +
            "WHERE t.tag = ?1")
    Set<Post> findPostsByTag(String tag);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "WHERE  p.person.id  NOT IN (:blockers) " +
            "AND p.isBlocked = false AND per.isDeleted = false AND p.postText LIKE  %:query% AND p.datetime <= CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC ")
    Page<Post> findPostsByTextContainingExcludingBlockers(String query, Pageable pageable, List<Integer> blockers);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id   " +
            "WHERE p.person.id NOT IN (:blockers) " +
            "AND p.isBlocked = false AND ( p.datetime >= :dateFrom AND p.datetime <= :dateTo ) AND p.datetime <= CURRENT_TIMESTAMP " +
            "AND ( p.postText LIKE '%'||:text||'%' AND :text != '' OR :text = '' ) " +
            "AND ( p.person.firstName LIKE :author||'%' AND :author != '' OR :author = '' ) " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContainingByDateExcludingBlockers(String text, String author, Instant dateFrom, Instant dateTo, Pageable pageable, List<Integer> blockers);

}