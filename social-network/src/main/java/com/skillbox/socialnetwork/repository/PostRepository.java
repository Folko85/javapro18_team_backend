package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

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
            "WHERE p.isBlocked = false AND per.isDeleted = false AND p.person.id = ?1 AND p.datetime <= CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByPersonIdAndCurrentDate(int id, Pageable pageable);


    @Query("SELECT p " +
            "FROM Post p " +
            "JOIN p.tags t " +
            "WHERE t.tag = :tag")
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
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "JOIN p.tags t " +
            "WHERE p.person.id NOT IN (:blockers) " +
            "AND t.id IN (:tags) " +
            "AND p.isBlocked = false " +
            "AND ( p.datetime >= :dateFrom AND p.datetime <= :dateTo ) AND p.datetime <= CURRENT_TIMESTAMP " +
            "AND (( p.postText LIKE '%'||:text||'%' OR p.title LIKE '%'||:text||'%') AND :text != '' OR :text = '' ) " +
            "AND ((p.person.firstName LIKE :author||'%' OR p.person.lastName LIKE :author||'%') AND :author != '' OR :author = '' ) " +
            "GROUP BY p.id " +
            "HAVING COUNT(t) = :tagsCount " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContainingByDateExcludingBlockers(String text, String author, Instant dateFrom, Instant dateTo,
                                                                Pageable pageable, List<Integer> blockers, List<Integer> tags, long tagsCount);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN Person per ON per.id = p.person.id " +
            "WHERE p.person.id NOT IN (:blockers) " +
            "AND p.isBlocked = false " +
            "AND ( p.datetime >= :dateFrom AND p.datetime <= :dateTo ) AND p.datetime <= CURRENT_TIMESTAMP " +
            "AND (( p.postText LIKE '%'||:text||'%' OR p.title LIKE '%'||:text||'%') AND :text != '' OR :text = '' ) " +
            "AND ((p.person.firstName LIKE :author||'%' OR p.person.lastName LIKE :author||'%') AND :author != '' OR :author = '' ) " +
            "GROUP BY p.id " +
            "ORDER BY p.datetime DESC")
    Page<Post> findPostsByTextContainingByDateExcludingBlockersWithoutTags(String text, String author, Instant dateFrom, Instant dateTo,
                                                                           Pageable pageable, List<Integer> blockers);
    @Query("SELECT p FROM Post p" +
            "WHERE deleted_at < :minusDays")
    List<Post> findSoftDeletedPostsID(@Param("minusDays") LocalDateTime minusDays);
}