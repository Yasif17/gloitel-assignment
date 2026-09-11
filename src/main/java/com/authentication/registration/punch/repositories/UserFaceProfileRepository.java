package com.authentication.registration.punch.repositories;

import com.authentication.registration.punch.entities.UserFaceProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFaceProfileRepository extends JpaRepository<UserFaceProfile, Long> {
    Optional<UserFaceProfile> findByUserId(Long userId);
}