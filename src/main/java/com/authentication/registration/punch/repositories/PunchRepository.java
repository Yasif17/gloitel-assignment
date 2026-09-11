package com.authentication.registration.punch.repositories;

import com.authentication.registration.punch.entities.PunchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PunchRepository extends JpaRepository<PunchRecord,Long> {

    Optional<PunchRecord> findByUserIdAndCampIdAndPunchDate(Long userId,Long campId, LocalDate punchDate); // get the punch-in record

}
