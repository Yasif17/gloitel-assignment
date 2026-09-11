package com.authentication.registration.punch.repositories;

import com.authentication.registration.punch.entities.mmuUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface mmuUserRepository extends JpaRepository<mmuUser,Long> {
}
