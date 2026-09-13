package com.blinkit.application.productAndCategory.services;

import com.blinkit.application.productAndCategory.dtos.requests.ProductRequest;
import com.blinkit.application.productAndCategory.dtos.response.ProductCardResponse;
import com.blinkit.application.productAndCategory.dtos.response.ProductDetailResponse;
import com.blinkit.application.productAndCategory.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    public Page<ProductCardResponse> search(Long storeId, Long categoryId, String keyword,
                                            Double minPrice, Double maxPrice, Pageable pageable);

    public ProductDetailResponse getDetail(String slug, Long storeId);

    public Product createProduct(ProductRequest productRequest);

    public Product updateProduct(Long id, ProductRequest productRequest);

    public void deactivateProduct(Long id);

}
