package com.blinkit.application.productAndCategory.services.Impl;

import com.blinkit.application.productAndCategory.dtos.requests.CategoryRequest;
import com.blinkit.application.productAndCategory.dtos.response.ProductCardResponse;
import com.blinkit.application.productAndCategory.entities.Category;
import com.blinkit.application.productAndCategory.exceptions.CategoryAlreadyExistsException;
import com.blinkit.application.productAndCategory.exceptions.CategoryNotFoundException;
import com.blinkit.application.productAndCategory.repositories.CategoryRepository;
import com.blinkit.application.productAndCategory.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllActive() {
        return categoryRepository.findByActiveTrue();
    }

    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new CategoryAlreadyExistsException("Category already exists: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(toSlug(request.getName()));
        category.setActive(true);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setSlug(toSlug(request.getName()));
        return categoryRepository.save(category);
    }

    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        category.setActive(false);   // soft delete — never hard-delete, products still reference it
        categoryRepository.save(category);
    }

    private String toSlug(String name) {
        return name.trim().toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");
    }
}
