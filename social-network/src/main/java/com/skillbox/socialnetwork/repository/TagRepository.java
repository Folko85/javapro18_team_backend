package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {


    Optional<Tag> findByTag(String tag);

    @Query("SELECT t " +
            "FROM Tag t " +
            "WHERE t.tag LIKE  %?1%" +
            "GROUP BY t.id ")
    Page<Tag> findTagsByTextContaining(String tag, Pageable pageable);
}