package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<PostFile, Integer> {
}
