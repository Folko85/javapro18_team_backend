package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Integer> {
}
