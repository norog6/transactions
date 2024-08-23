package com.softclub.trans.repository;

import com.softclub.trans.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileREpositry extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByName(String name);
}
