package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<PostFile, Integer> {

    PostFile findByUrl(String url);

    Long removeByUrl(String url);
}
