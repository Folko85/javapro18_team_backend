package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {


    Optional<Tag> findByTag(String tag);

}
