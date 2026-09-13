package com.blinkit.application.productAndCategory.repositories;

import com.blinkit.application.productAndCategory.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);
}
