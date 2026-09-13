package com.blinkit.application.productAndCategory.services;

import com.blinkit.application.productAndCategory.dtos.requests.CategoryRequest;
import com.blinkit.application.productAndCategory.dtos.response.ProductCardResponse;
import com.blinkit.application.productAndCategory.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllActive();

    Category createCategory(CategoryRequest categoryRequest);

    Category updateCategory(Long categoryId, CategoryRequest categoryRequest);

    void deactivateCategory(Long categoryId);

}
