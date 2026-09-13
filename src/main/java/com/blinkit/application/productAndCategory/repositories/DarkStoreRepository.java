package com.blinkit.application.productAndCategory.repositories;

import com.blinkit.application.productAndCategory.entities.DarkStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DarkStoreRepository extends JpaRepository<DarkStore, Long> {
}
