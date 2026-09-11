package com.authentication.registration.punch.repositories;

import com.authentication.registration.punch.entities.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampRepository extends JpaRepository<Camp,Long> {
}
