package com.blinkit.application.productAndCategory.services.Impl;

import com.blinkit.application.productAndCategory.dtos.requests.ProductRequest;
import com.blinkit.application.productAndCategory.dtos.response.ProductCardResponse;
import com.blinkit.application.productAndCategory.dtos.response.ProductDetailResponse;
import com.blinkit.application.productAndCategory.entities.Category;
import com.blinkit.application.productAndCategory.entities.Inventory;
import com.blinkit.application.productAndCategory.entities.Product;
import com.blinkit.application.productAndCategory.exceptions.CategoryNotFoundException;
import com.blinkit.application.productAndCategory.exceptions.ProductNotFoundException;
import com.blinkit.application.productAndCategory.repositories.CategoryRepository;
import com.blinkit.application.productAndCategory.repositories.InventoryRepository;
import com.blinkit.application.productAndCategory.repositories.ProductRepository;
import com.blinkit.application.productAndCategory.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Page<ProductCardResponse> search(Long storeId, Long categoryId, String keyword,
                                       Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.search(storeId, categoryId, keyword, minPrice, maxPrice, pageable);
    }

    public ProductDetailResponse getDetail(String slug, Long storeId) {
        Product product = productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Availability is store-specific — same product, different stock per store
        int availableQty = inventoryRepository
                .findByProductIdAndDarkStoreId(product.getId(), storeId)
                .map(Inventory::getQuantity)
                .orElse(0);

        return new ProductDetailResponse(
                product.getId(), product.getSlug(), product.getName(), product.getImage(),
                product.getDescription(), product.getMrp(), product.getSellingPrice(),
                product.getUnit(), product.getCategory().getName(), availableQty
        );
    }

    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Product product = new Product();
        product.setCategory(category);
        product.setName(request.getName());
        product.setSlug(toSlug(request.getName()));
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        product.setUnit(request.getUnit());
        product.setActive(true);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        product.setCategory(category);
        product.setName(request.getName());
        product.setSlug(toSlug(request.getName()));
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        product.setUnit(request.getUnit());
        return productRepository.save(product);
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    private String toSlug(String name) {
        return name.trim().toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");
    }
}
